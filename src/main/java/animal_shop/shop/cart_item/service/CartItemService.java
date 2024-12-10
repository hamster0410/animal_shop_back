package animal_shop.shop.cart_item.service;

import animal_shop.community.member.entity.Member;
import animal_shop.community.member.repository.MemberRepository;
import animal_shop.global.security.TokenProvider;
import animal_shop.shop.cart_item.dto.CartItemSearchDTO;
import animal_shop.shop.cart_item.dto.CartItemSearchResponse;
import animal_shop.shop.cart_item.repository.CartItemRepository;
import animal_shop.shop.order_item.dto.MyItemDTO;
import animal_shop.shop.order_item.dto.OrderedItemInfo;
import animal_shop.shop.order_item.dto.OrderedOptionInfo;
import animal_shop.shop.order_item.dto.OrderedItemInfoList;
import animal_shop.shop.order_item.repository.OrderItemRepository;
import animal_shop.shop.point.dto.ItemProfitInfo;
import animal_shop.shop.point.dto.MyPointDTO;
import animal_shop.shop.point.dto.PointProfitDTO;
import animal_shop.shop.point.dto.PointProfitDTOResponse;
import animal_shop.shop.point.repository.PointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    OrderItemRepository orderItemRepository;

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

    public OrderedItemInfoList OrderedItemInfo(String token, Integer year, Integer month, Integer day) {
        String userId = tokenProvider.extractIdByAccessToken(token);

        List<Object[]> objects = pointRepository.findTotalItemByOrderItem(Long.valueOf(userId),year,month,day);

        HashMap<String, List<OrderedOptionInfo>> hashMap = new HashMap<>();
        List<OrderedItemInfo> orderedItemInfoList = new ArrayList<>();
        for(Object[] obj : objects){
            System.out.println(obj[0] + " " + obj[1] + " " + obj[2] + " " +obj[3]);
            if(hashMap.containsKey((String)obj[2])){
                hashMap.get((String)obj[2]).add(new OrderedOptionInfo(obj));
            }else{
                hashMap.put((String)obj[2],new ArrayList<>());
                hashMap.get((String)obj[2]).add(new OrderedOptionInfo(obj));
            }
        }
        for(String key : hashMap.keySet()){
            orderedItemInfoList.add(new OrderedItemInfo(key, hashMap.get(key)));
        }

//0. 날짜
//1. 옵션 이름
//2. 아이템 이름
//3. 갯수
        String s_year;
        String s_month;
        String s_day;

        if(year != null) s_year =year.toString(); else s_year="*";
        if(month != null) s_month =month.toString(); else s_month="*";
        if(day != null) s_day =day.toString(); else s_day = "*";
        return OrderedItemInfoList.builder()
                .orderedOptionInfoList(orderedItemInfoList)
                .date(s_year + " : " + s_month + " : " + s_day)
                .first_date(orderItemRepository.findEarliestOrderItemDate())
                .build();
    }

    public List<MyItemDTO> entireCartItemInfo(String token, String time, String start, String end) {
        String userId = tokenProvider.extractIdByAccessToken(token);
        List<Object[]> objects;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        if(end!=null){
            LocalDateTime endDateTime = LocalDate.parse(end, formatter).atStartOfDay().plusDays(1).minusSeconds(1);
            end = endDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }

        if(start!=null){
            LocalDateTime startDateTime = LocalDate.parse(start, formatter).atStartOfDay();
            start = startDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }

        if (time == null) {
            objects =  cartItemRepository.cartItemForAll(Long.valueOf(userId));
        } else {
            objects =  cartItemRepository.cartItemForTime(time,Long.valueOf(userId),start,end);
        }
        List<MyItemDTO> myItemDTOList = new ArrayList<>();
        for(Object[] obj : objects){
            myItemDTOList.add(new MyItemDTO(obj));
        }
        return myItemDTOList;
    }
}
