package animal_shop.shop.cart_item.service;

import animal_shop.community.member.entity.Member;
import animal_shop.community.member.repository.MemberRepository;
import animal_shop.global.security.TokenProvider;
import animal_shop.shop.cart_item.dto.CartItemSearchDTO;
import animal_shop.shop.cart_item.dto.CartItemSearchResponse;
import animal_shop.shop.cart_item.repository.CartItemRepository;
import animal_shop.shop.item.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


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

    public CartItemSearchResponse cartItemInfo(String token, Integer year, Integer month) {
        System.out.println("service " + year + " " + month);
        String userId = tokenProvider.extractIdByAccessToken(token);
        Member member = memberRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new IllegalArgumentException("member is not found"));

        List<Object[]> objects = cartItemRepository.countItemsByMemberAndDate(member, year, month);
        List<CartItemSearchDTO> cartItemSearchDTOList = new ArrayList<>();

        for(Object[] obj : objects){
            cartItemSearchDTOList.add(new CartItemSearchDTO(obj,year,month));
        }
        CartItemSearchResponse cartItemSearchResponse = new CartItemSearchResponse();
        cartItemSearchResponse.setCartItemSearchDTOList(cartItemSearchDTOList);
        return cartItemSearchResponse;
    }
}
