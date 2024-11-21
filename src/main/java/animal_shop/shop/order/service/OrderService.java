package animal_shop.shop.order.service;

import animal_shop.community.member.entity.Member;
import animal_shop.community.member.repository.MemberRepository;
import animal_shop.global.security.TokenProvider;
import animal_shop.shop.item.entity.Item;
import animal_shop.shop.item.repository.ItemRepository;
import animal_shop.shop.order.dto.OrderDTO;
import animal_shop.shop.order.entity.Order;
import animal_shop.shop.order.repository.OrderRepository;
import animal_shop.shop.order_item.dto.OrderHistDTO;
import animal_shop.shop.order_item.dto.OrderHistDTOResponse;
import animal_shop.shop.order_item.dto.OrderItemDTO;
import animal_shop.shop.order_item.entity.OrderItem;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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
    private TokenProvider tokenProvider;

    public Long order(OrderDTO orderDTO, String token){
        //내가 주문할 상품 찾기
        Item item = itemRepository.findById(orderDTO.getItemId())
                .orElseThrow(EntityNotFoundException::new);

        //주문한 사람이 누구인지 나타냄
        String userId = tokenProvider.extractIdByAccessToken(token);
        Member member = memberRepository.findById(Long.valueOf(userId))
                .orElseThrow(EntityNotFoundException::new);

        //주문할 상품 엔티티와 수량을 이용해 주문을 생성함
        List<OrderItem> orderItemList = new ArrayList<>();
        OrderItem orderItem =
                OrderItem.createOrderItem(item, orderDTO);

        Order order = Order.createOrder(member, orderItemList);
        //생성한 주문 엔티티를 저장함
        orderItem.setOrder(order);

        orderRepository.save(order);

        return order.getId();
    }

    @Transactional(readOnly = true)
    public OrderHistDTOResponse getOrderList(String token, int page){
        String userId = tokenProvider.extractIdByAccessToken(token);

        Pageable pageable = (Pageable) PageRequest.of(page,10);

        Page<Order> orders = orderRepository.findOrders(userId, pageable);
        Long total_count = orders.getTotalElements();

        List<OrderHistDTO> orderHistDTOs = new ArrayList<>();

        for(Order order : orders){
            System.out.println(order.getOrderItems());
            OrderHistDTO orderHistDTO = new OrderHistDTO(order);
            List<OrderItem> orderItems = order.getOrderItems();;
            for(OrderItem orderItem : orderItems){

                System.out.println(orderItem.getItem().getThumbnail_url());

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
}
