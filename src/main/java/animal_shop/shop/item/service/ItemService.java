package animal_shop.shop.item.service;

import animal_shop.community.member.dto.TokenDTO;
import animal_shop.community.member.entity.Member;
import animal_shop.community.member.repository.MemberRepository;
import animal_shop.global.security.TokenProvider;
import animal_shop.shop.item.dto.ItemDTOList;
import animal_shop.shop.item.entity.Item;
import animal_shop.shop.item.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ItemService {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private MemberRepository memberRepository;

    public void save(String token, ItemDTOList itemDTOList) {
        String userId = tokenProvider.extractIdByAccessToken(token);

        Member member = memberRepository.findById(Long.valueOf(userId)).orElseThrow(() -> new IllegalArgumentException("member is not found"));

        if(!member.getRole().toString().equals("SELLER")){
            throw  new IllegalStateException("is not seller");
        }

        Item item = Item.builder()
                .name(itemDTOList.getName())
                .category(itemDTOList.getCategory())
                .comment_count(0L)
                .species(itemDTOList.getCategory())
                .stock_number(itemDTOList.getStock_number())
                .itemSellStatus(itemDTOList.getSell_status())
                .member(member)
                .image_url(itemDTOList.getImageUrl())
                .thumbnail_url(itemDTOList.getThumbnailUrls())
                .options(itemDTOList.getOption())
                .build();

        itemRepository.save(item);
    }
}
