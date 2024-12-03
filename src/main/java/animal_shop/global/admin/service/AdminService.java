package animal_shop.global.admin.service;

import animal_shop.community.member.Role;
import animal_shop.community.member.entity.Member;
import animal_shop.community.member.entity.SellerCandidate;
import animal_shop.community.member.repository.MemberRepository;
import animal_shop.community.member.repository.SellerCandidateRepository;
import animal_shop.global.admin.dto.SellerDTO;
import animal_shop.global.admin.dto.SellerResponseDTO;
import animal_shop.global.admin.dto.StopItemDTO;
import animal_shop.global.admin.entity.StopItem;
import animal_shop.global.admin.repository.StopItemRepository;
import animal_shop.global.security.TokenProvider;
import animal_shop.shop.item.ItemSellStatus;
import animal_shop.shop.item.entity.Item;
import animal_shop.shop.item.repository.ItemRepository;
import animal_shop.shop.main.dto.MainDTO;
import animal_shop.shop.main.dto.MainDTOBestResponse;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminService {

    @Autowired
    private SellerCandidateRepository sellerCandidateRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private StopItemRepository stopItemRepository;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private JavaMailSenderImpl javaMailSenderImpl;

    @Transactional
    public SellerResponseDTO request_list(int page) {
        Pageable pageable = (Pageable) PageRequest.of(page,10);
        Page<SellerCandidate> sellerCandidates = sellerCandidateRepository.findAll(pageable);

       List<SellerDTO> sellerDTOS = sellerCandidates.getContent().stream()
                .map(SellerDTO::new)
                .collect(Collectors.toList());

       return  SellerResponseDTO
               .builder()
               .sellerDTOS(sellerDTOS)
               .totalCount(sellerCandidates.getTotalElements())
               .build();
    }
    @Transactional
    public void permit_seller(String token, String username) {
        String userId = tokenProvider.extractIdByAccessToken(token);
        Member admin = memberRepository.findById(Long.valueOf(userId)).orElseThrow(() -> new IllegalArgumentException("Member does not exist with ID: "));

        Member user = memberRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("Member does not exist with USER NAME: "+username));

        //admin 인지 아닌지 판별
        if(!admin.getRole().toString().equals("ADMIN")){
            throw new IllegalArgumentException("member is not admin ");
        }

        user.setRole(Role.SELLER);
        memberRepository.save(user);
    }
    @Transactional
    public void revoke_seller(String token, String username) {
        String userId = tokenProvider.extractIdByAccessToken(token);
        Member admin = memberRepository.findById(Long.valueOf(userId)).orElseThrow(() -> new IllegalArgumentException("Member does not exist with ID: "));

        //admin 인지 아닌지 판별
        if(!admin.getRole().toString().equals("ADMIN")){
            throw new IllegalArgumentException("member is not admin ");
        }

        Member user = memberRepository.findByUsername(username)
                    .orElseThrow(() -> new IllegalArgumentException("Member does not exist with ID: "));
        user.setRole(Role.USER);
        memberRepository.save(user);
    }
    @Transactional
    public void delete_seller(String token, String username) {
        String userId = tokenProvider.extractIdByAccessToken(token);
        Member admin = memberRepository.findById(Long.valueOf(userId)).orElseThrow(() -> new IllegalArgumentException("Member does not exist with ID: "));

        //admin 인지 아닌지 판별
        if(!admin.getRole().toString().equals("ADMIN")){
            throw new IllegalArgumentException("member is not admin ");
        }

        Member user = memberRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Member does not exist with ID: "));

        SellerCandidate sellerCandidate = sellerCandidateRepository.findByMember(user).get(0);

        sellerCandidateRepository.delete(sellerCandidate);
    }

    @Transactional
    public void stop_item(String token, StopItemDTO stopItemDTO) {
        String userId = tokenProvider.extractIdByAccessToken(token);
        Member admin = memberRepository.findById(Long.valueOf(userId)).orElseThrow(() -> new IllegalArgumentException("Member does not exist with ID: "));

        //admin 인지 아닌지 판별
        if(!admin.getRole().toString().equals("ADMIN")){
            throw new IllegalArgumentException("member is not admin ");
        }

        Item item = itemRepository.findById(stopItemDTO.getItemId())
                .orElseThrow(() -> new IllegalArgumentException("item is not found"));
        item.setItemSellStatus(ItemSellStatus.valueOf("STOP"));

        Member seller = item.getMember();

        //메세지 전송
        final MimeMessagePreparator mimeMessagePreparator = new MimeMessagePreparator() {
            public void prepare(MimeMessage mimeMessage) throws Exception {

                final MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
                mimeMessageHelper.setTo(seller.getMail());
                mimeMessageHelper.setSubject(seller.getNickname() + "님의 상품 " + item.getName() + "이 다음과 같은 이유로 판매 중단 되었습니다.");
                mimeMessageHelper.setText(stopItemDTO.getSuspensionReason());
            };

        };javaMailSenderImpl.send(mimeMessagePreparator);

        StopItem stopItem = new StopItem(item, admin, seller, stopItemDTO);
        stopItemRepository.save(stopItem);
    }

    @Transactional(readOnly = true)
    public MainDTOBestResponse stop_list(String token, int page) {
        String userId = tokenProvider.extractIdByAccessToken(token);
        Member admin = memberRepository.findById(Long.valueOf(userId)).orElseThrow(() -> new IllegalArgumentException("Member does not exist with ID: "));
        Pageable pageable = (Pageable) PageRequest.of(page,20);
        //admin 인지 아닌지 판별
        if(!admin.getRole().toString().equals("ADMIN")){
            throw new IllegalArgumentException("member is not admin ");
        }
        Page<Item> items = itemRepository.findByItemSellStatus(ItemSellStatus.STOP,pageable);
        return MainDTOBestResponse.builder()
                .goods(items.stream().map(MainDTO::new).toList())
                .total_count(items.getTotalElements())
                .build();
    }
}
