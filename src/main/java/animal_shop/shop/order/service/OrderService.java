package animal_shop.shop.order.service;

import animal_shop.community.member.entity.Member;
import animal_shop.community.member.repository.MemberRepository;
import animal_shop.global.security.TokenProvider;
import animal_shop.shop.cart.dto.CartDetailDTO;
import animal_shop.shop.cart.dto.CartDetailDTOResponse;
import animal_shop.shop.delivery.dto.DeliveryRevokeDTO;
import animal_shop.shop.delivery.dto.DeliveryRevokeResponse;
import animal_shop.shop.delivery.entity.Delivery;
import animal_shop.shop.delivery.entity.DeliveryItem;
import animal_shop.shop.delivery.repository.DeliveryItemRepository;
import animal_shop.shop.delivery.repository.DeliveryRepository;
import animal_shop.shop.delivery.service.DeliveryService;
import animal_shop.shop.item.entity.Item;
import animal_shop.shop.item.repository.ItemRepository;
import animal_shop.shop.order.dto.OrderDTO;
import animal_shop.shop.order.dto.OrderDTOList;
import animal_shop.shop.order.entity.Order;
import animal_shop.shop.order.repository.OrderRepository;
import animal_shop.shop.order_item.dto.OrderHistDTO;
import animal_shop.shop.order_item.dto.OrderHistDTOResponse;
import animal_shop.shop.order_item.dto.OrderItemDTO;
import animal_shop.shop.order_item.entity.OrderItem;
import animal_shop.shop.order_item.repository.OrderItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private DeliveryRepository deliveryRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private DeliveryItemRepository deliveryItemRepository;
    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private DeliveryService deliveryService;



    @Transactional
    public void order(OrderDTOList orderDTOList, String token){
        //내가 주문할 상품 찾기
        Item item = itemRepository.findById(orderDTOList.getItemId())
                .orElseThrow(() -> new IllegalArgumentException("item not found"));


        //주문한 사람이 누구인지 나타냄
        String userId = tokenProvider.extractIdByAccessToken(token);
        Member member = memberRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new IllegalArgumentException("member not found"));

        //주문할 상품 엔티티와 수량을 이용해 주문을 생성함
        List<OrderItem> orderItemList = new ArrayList<>();
        List<OrderDTO> orderDTOS = orderDTOList.getOption_items();


        for(OrderDTO o : orderDTOS){
            OrderItem orderItem =
                    OrderItem.createOrderItem(item, o);
            //여기서 orderItem을 넣어주면 orderItem 테이블에도 저장되는지 확인해보자

            orderItemList.add(orderItem);
        }


        Order order = Order.createOrder(member, orderItemList);
        order.setTid(orderDTOList.getTid());
        //생성한 주문 엔티티를 저장함
        orderRepository.save(order);

        deliveryService.createDelivery(orderItemList.get(0).getItem().getMember(), orderItemList ,order);
    }

    @Transactional(readOnly = true)
    public OrderHistDTOResponse getOrderList(String token, int page){
        String userId = tokenProvider.extractIdByAccessToken(token);

        Pageable pageable = (Pageable) PageRequest.of(page,10);

        Page<Order> orders = orderRepository.findOrders(userId, pageable);
        Long total_count = orders.getTotalElements();

        List<OrderHistDTO> orderHistDTOs = new ArrayList<>();

        for(Order order : orders){
            OrderHistDTO orderHistDTO = new OrderHistDTO(order);
            List<OrderItem> orderItems = order.getOrderItems();

            for(OrderItem orderItem : orderItems){
                OrderItemDTO orderItemDTO = new OrderItemDTO(orderItem, orderItem.getItem().getThumbnail_url().get(0));

                orderHistDTO.addOrderItemDTO(orderItemDTO);
            }

            orderHistDTOs.add(orderHistDTO  );
        }
        return OrderHistDTOResponse.builder()
                .orderHistDTOList(orderHistDTOs)
                .total_count(total_count)
                .build();
    }

    @Transactional
    public DeliveryRevokeResponse cancelOrder(String token, Long orderId){
        String userId = tokenProvider.extractIdByAccessToken(token);
        Member curMember = memberRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new IllegalArgumentException("member not found"));
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("order not found"));
        if(!curMember.getUsername().equals(order.getMember().getUsername())){
            throw new IllegalArgumentException("validate false");
        }

        List<Delivery> deliveries = deliveryRepository.findByOrderId(orderId);

        for (Delivery delivery : deliveries) {
            for (DeliveryItem deliveryItem : delivery.getDeliveryItems()) {
                deliveryItem.setDelivery_revoke(true);
            }
        }
// 별도의 save 호출 없이 Transaction 종료 시점에 변경 사항 반영
        int total_count = 0;
        long total_amount = 0L;
        for(OrderItem orderItem : order.getOrderItems()){
            //이미 주문 취소한 품목의 금액은 책정하지 않음
            if(!orderItem.isDelivery_revoke()){
                total_count += orderItem.getCount();
                total_amount = (long) orderItem.getOrder_price() * orderItem.getCount();
            }
        }

        order.cancelOrder();

        return DeliveryRevokeResponse.builder()
                .tid(order.getTid())
                .itemName(order.getOrderItems().get(0).getOrder_name() + "외 " + (order.getOrderItems().size() -1) + "건")
                .itemQuantity(String.valueOf(total_count))
                .cancelAmount(total_amount)
                .build();

    }

    @Transactional
    public DeliveryRevokeResponse cancelOrderDetail(String token, DeliveryRevokeDTO deliveryRevokeDTO) {

        long userId = Long.parseLong(tokenProvider.extractIdByAccessToken(token));
        OrderItem orderItem = null;

        int itemQuantity = 0;
        long cancelAmount = 0;

        for(Long orderItemId : deliveryRevokeDTO.getOrderItemIds()){
            orderItem = orderItemRepository.findById(orderItemId)
                    .orElseThrow(() -> new IllegalArgumentException("order item not found"));

            itemQuantity++;
            cancelAmount += (long) orderItem.getOrder_price() * orderItem.getCount();

            orderItem.cancel();

            DeliveryItem deliveryItem = deliveryItemRepository.findByOrderItemId(orderItemId);

            //물품이 구매자가 산 물품인지 검증
            if(!deliveryItem.getBuyerId().equals(userId)){
                throw new IllegalArgumentException("buyer is not matching");
            }
            deliveryItem.setDelivery_revoke(true);
//            deliveryItemRepository.save(deliveryItem);
        }

//        orderItem.setDelivery_revoke(true);

//        orderItemRepository.save(orderItem);


        String item_name = Objects.requireNonNull(orderItem).getOrder_name();

        if(!(itemQuantity==1)){
            item_name += "외 " + (itemQuantity -1) + "건";
        }

        return DeliveryRevokeResponse.builder()
                .tid(orderItem.getOrder().getTid())
                .itemQuantity(String.valueOf(itemQuantity))
                .cancelAmount(cancelAmount)
                .itemName(item_name)
                .build();
    }

    @Transactional
    public void orderCart(String token, CartDetailDTOResponse cartDetailDTOResponse) {
        List<CartDetailDTO> cartDetailDTOList = cartDetailDTOResponse.getCartDetailDTOList();

        //카트 비었을때 에러처리
        if(cartDetailDTOList.isEmpty()){
            throw new IllegalArgumentException("cart is null");
        }

        //주문한 사람이 누구인지 나타냄
        String userId = tokenProvider.extractIdByAccessToken(token);
        Member member = memberRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new IllegalArgumentException("member not found"));

        //카트아이템에서 정보를 끄집어 내서 order에 저장
        List<OrderItem> orderItemList = new ArrayList<>();

        //각각에 판매자들에게 따로 전송
        HashMap<Member,List<OrderItem>> hashMap = new HashMap<>();

        for( CartDetailDTO c : cartDetailDTOList){
            Item item = itemRepository.findById(c.getItemId())
                    .orElseThrow(() -> new IllegalArgumentException("item is not found"));

            OrderDTO orderDTO = new OrderDTO(c.getCount(), c.getOption_name(), Math.toIntExact(c.getOption_price()));

            OrderItem orderItem = OrderItem.createOrderItem(item,orderDTO);

            //각각에 판매자에게 배송 테이블 전송
            if(hashMap.containsKey(orderItem.getItem().getMember())){
                hashMap.get(orderItem.getItem().getMember()).add(orderItem);
            }else{
                hashMap.put(orderItem.getItem().getMember(), new ArrayList<OrderItem>());
                hashMap.get(orderItem.getItem().getMember()).add(orderItem);
            }
            orderItemList.add(orderItem);
        }

        Order order = Order.createOrder(member,orderItemList);
        order.setTid(cartDetailDTOResponse.getTid());
        orderRepository.save(order);

        //판매자에게 배송 정보 전달
        for(Member m : hashMap.keySet()){
            deliveryService.createDelivery(m, hashMap.get(m),order);
        }

    }

}
