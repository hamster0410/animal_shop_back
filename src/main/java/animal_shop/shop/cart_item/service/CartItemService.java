package animal_shop.shop.cart_item.service;

import animal_shop.community.member.entity.Member;
import animal_shop.community.member.repository.MemberRepository;
import animal_shop.global.security.TokenProvider;
import animal_shop.shop.cart_item.dto.CartItemSearchResponse;
import animal_shop.shop.cart_item.repository.CartItemRepository;
import animal_shop.shop.item.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class CartItemService {

    @Autowired
    TokenProvider tokenProvider;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    CartItemRepository cartItemRepository;

    @Autowired
    ItemRepository itemRepository;

    public CartItemSearchResponse cartItemInfo(String token, int year, int month) {
        String userId = tokenProvider.extractIdByAccessToken(token);
        Member member = memberRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new IllegalArgumentException("member is not found"));
//        List<Item> itemList = itemRepository.findByMemberId(userId);
//        List<Object[]> objects = cartItemRepository.countCartItemsByItemAndMemberAndDate(member, year, month, itemList);
//        for(Object[] object : objects){
//            System.out.println(object[0] + " " + object[1] );
//        }
        return null;
    }
}
