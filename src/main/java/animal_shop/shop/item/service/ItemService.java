package animal_shop.shop.item.service;

import animal_shop.community.member.entity.Member;
import animal_shop.community.member.repository.MemberRepository;
import animal_shop.global.security.TokenProvider;
import animal_shop.shop.item.dto.ItemDTOList;
import animal_shop.shop.item.dto.ItemDetailDTO;
import animal_shop.shop.item.entity.Item;
import animal_shop.shop.item.entity.Option;
import animal_shop.shop.item.repository.ItemRepository;
import animal_shop.shop.item.repository.OptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ItemService {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private OptionRepository optionRepository;

    @Transactional
    public void save(String token, ItemDTOList itemDTOList) {
        String userId = tokenProvider.extractIdByAccessToken(token);

        Member member = memberRepository.findById(Long.valueOf(userId)).orElseThrow(() -> new IllegalArgumentException("member is not found"));
// seller가 아닌 경우 예외 처리
        if (!member.getRole().toString().equals("SELLER")) {
            throw new IllegalStateException("User is not a seller");
        }

        // Option 리스트 가져오기
        List<Option> options = itemDTOList.getOption();

        // Item 객체 생성
        Item item = Item.builder()
                .name(itemDTOList.getName())
                .category(itemDTOList.getCategory())
                .comment_count(0L)
                .species(itemDTOList.getSpecies())
                .stock_number(itemDTOList.getStock_number())
                .itemSellStatus(itemDTOList.getSell_status())
                .member(member)
                .image_url(itemDTOList.getImageUrl())
                .thumbnail_url(itemDTOList.getThumbnailUrls())
                .build();

        // Item 저장
        Item savedItem = itemRepository.save(item);  // 여기서 item이 저장되고 id가 생성됨
        // 옵션을 저장할 때, 각각의 Option에 item_id 설정
        for (Option option : options) {
            option.setItem(savedItem); // 저장된 Item의 id를 Option에 설정
        }

        // Option 저장
        optionRepository.saveAll(options); // 여러 개의 Option 저장
    }


    public void update(String token, ItemDTOList itemDTOList) {
        // 1. 사용자 인증 (SELLER 권한 확인)
        String userId = tokenProvider.extractIdByAccessToken(token);
        Member member = memberRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new IllegalArgumentException("member is not found"));

        if(!member.getRole().toString().equals("SELLER")){
            throw new IllegalStateException("is not seller");
        }

        // 2. 수정할 아이템 인증 (아이템 ID로 조회)
        Item item = itemRepository.findById(itemDTOList.getId())
                .orElseThrow(() -> new IllegalArgumentException("Item not found"));

        // 3. 아이템 속성 수정
        item.setName(itemDTOList.getName());
        item.setCategory(itemDTOList.getCategory());
        item.setStock_number(itemDTOList.getStock_number());
        item.setItemSellStatus(itemDTOList.getSell_status());
        item.setSpecies(itemDTOList.getSpecies());
        item.setThumbnail_url(itemDTOList.getThumbnailUrls());
        item.setImage_url(itemDTOList.getImageUrl());
        item.setOptions(itemDTOList.getOption()); // 옵션 수정

        // 4. 수정된 아이템 저장
        itemRepository.save(item);
    }

    public ItemDetailDTO findById(String itemId) {
        Item item = itemRepository.findById(Long.valueOf(itemId))
                .orElseThrow(() -> new IllegalArgumentException("item not found"));

        return new ItemDetailDTO(item);
    }
}
