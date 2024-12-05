package animal_shop.shop.cart_item.service;

import animal_shop.community.member.entity.Member;
import animal_shop.community.member.repository.MemberRepository;
import animal_shop.global.security.TokenProvider;
import animal_shop.shop.cart_item.dto.CartItemSearchDTO;
import animal_shop.shop.cart_item.dto.CartItemSearchResponse;
import animal_shop.shop.cart_item.repository.CartItemRepository;
import animal_shop.shop.item.repository.ItemRepository;
import animal_shop.shop.point.dto.ItemProfitInfo;
import animal_shop.shop.point.dto.PointProfitDTO;
import animal_shop.shop.point.dto.PointProfitDTOResponse;
import animal_shop.shop.point.repository.PointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
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
    PointRepository pointRepository;

    @Autowired
    ItemRepository itemRepository;

    public CartItemSearchResponse cartItemInfo(String token, Integer year, Integer month) {
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

    public PointProfitDTOResponse ProfitItemInfo(String token, Integer year, Integer month, Integer day) {
        String userId = tokenProvider.extractIdByAccessToken(token);

        List<PointProfitDTO> profitDTOList = new ArrayList<>();

        List<Object[]> objects = pointRepository.findTotalPointsByItemIds(Long.valueOf(userId),year,month,day);

        HashMap<String,List<PointProfitDTO>> hashMap = new HashMap<>();
        List<ItemProfitInfo> itemProfitInfoList = new ArrayList<>();

        for(Object[] obj : objects){
            if(hashMap.containsKey((String)obj[1])){
                hashMap.get((String)obj[1]).add(new PointProfitDTO(obj));
            }else{
                hashMap.put((String)obj[1],new ArrayList<>());
                hashMap.get((String)obj[1]).add(new PointProfitDTO(obj));
            }
            profitDTOList.add(new PointProfitDTO(obj));
        }

        for(String key : hashMap.keySet()){
            itemProfitInfoList.add(new ItemProfitInfo(key, hashMap.get(key)));
        }
        String s_year;
        String s_month;
        String s_day;

        if(year != null) s_year =year.toString(); else s_year="*";
        if(month != null) s_month =month.toString(); else s_month="*";
        if(day != null) s_day =day.toString(); else s_day = "*";
        PointProfitDTOResponse pointProfitDTOResponse = PointProfitDTOResponse.builder()
                .date(s_year + " : " + s_month + " : " + s_day)
                .itemProfitInfoList(itemProfitInfoList)
                .first_date(pointRepository.findEarliestPointDate())
                .build();

        return pointProfitDTOResponse;
    }
}
