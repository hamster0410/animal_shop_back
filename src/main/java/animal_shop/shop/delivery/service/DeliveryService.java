package animal_shop.shop.delivery.service;

import animal_shop.community.member.entity.Member;
import animal_shop.community.member.repository.MemberRepository;
import animal_shop.global.security.TokenProvider;
import animal_shop.shop.delivery.dto.DeliveryApproveRequestDTO;
import animal_shop.shop.delivery.dto.DeliveryDTO;
import animal_shop.shop.delivery.dto.DeliveryDTOResponse;
import animal_shop.shop.delivery.entity.Delivery;
import animal_shop.shop.delivery.entity.DeliveryItem;
import animal_shop.shop.delivery.repository.DeliveryRepository;
import animal_shop.shop.order.entity.Order;
import animal_shop.shop.order.repository.OrderRepository;
import animal_shop.shop.order_item.entity.OrderItem;
import animal_shop.shop.order_item.repository.OrderItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DeliveryService {

    @Autowired
    TokenProvider tokenProvider;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderItemRepository orderItemRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    DeliveryRepository deliveryRepository;

    @Transactional(readOnly = true)
    public DeliveryDTOResponse get_list(String token, int page) {
        String userId = tokenProvider.extractIdByAccessToken(token);
        Member member = memberRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new IllegalArgumentException("member not found"));
        Pageable pageable = (Pageable) PageRequest.of(page,10);

        Page<Delivery> deliveries = deliveryRepository.findByMember(member, pageable);

        return DeliveryDTOResponse.builder()
                .deliveryDTOList(deliveries.stream().map(DeliveryDTO::new).toList())
                .total_count(deliveries.getTotalElements())
                .build();

    }

    @Transactional
    public void approve(DeliveryApproveRequestDTO deliveryApproveRequestDTO, String token) {
        String userId = tokenProvider.extractIdByAccessToken(token);
        Member member = memberRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new IllegalArgumentException("member not found"));

        Order order = orderRepository.findById(deliveryApproveRequestDTO.getOrderId())
                .orElseThrow(() -> new IllegalArgumentException("order not found"));

        List<OrderItem> orderItems = order.getOrderItems();
        for(OrderItem orderItem : orderItems){
            if(orderItem.getItem().getMember().equals(member)){
                orderItem.setDelivery_approval(true);
            }
        }

        Delivery delivery = deliveryRepository.findById(deliveryApproveRequestDTO.getDeliveryId())
                .orElseThrow(() -> new IllegalArgumentException("delivery is not found"));
        for(DeliveryItem d: delivery.getDeliveryItems()) {
            d.setDelivery_approval(true);
        }
    }

    @Transactional
    public void approve_detail(Long orderItemId, String token) {
        String userId = tokenProvider.extractIdByAccessToken(token);
        Member member = memberRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new IllegalArgumentException("member not found"));
        OrderItem orderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> new IllegalArgumentException("order item not found"));
        orderItem.setDelivery_approval(true);
        orderItemRepository.save(orderItem);

    }

    public void createDelivery(Member m, List<OrderItem> orderItems, Order order) {
        Delivery delivery = new Delivery(m, orderItems, order);
        deliveryRepository.save(delivery);
    }

    //카카오 페이 실패 시 결제 삭제
    public void removeOrder(Order order) {
        List<Delivery> delivery = deliveryRepository.findByOrderId(order.getId());
        for(Delivery d : delivery){
            deliveryRepository.delete(d);
        }
    }

}
