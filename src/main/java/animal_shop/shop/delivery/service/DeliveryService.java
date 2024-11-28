package animal_shop.shop.delivery.service;

import animal_shop.community.member.entity.Member;
import animal_shop.community.member.repository.MemberRepository;
import animal_shop.global.security.TokenProvider;
import animal_shop.shop.delivery.dto.*;
import animal_shop.shop.delivery.entity.Delivery;
import animal_shop.shop.delivery.entity.DeliveryItem;
import animal_shop.shop.delivery.repository.DeliveryItemRepository;
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
import java.util.Objects;

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

    @Autowired
    DeliveryItemRepository deliveryItemRepository;

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
    public void approve(DeliveryRequestDTO deliveryRequestDTO, String token) {
        String userId = tokenProvider.extractIdByAccessToken(token);
        Member member = memberRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new IllegalArgumentException("member not found"));

        Delivery delivery = deliveryRepository.findById(deliveryRequestDTO.getDeliveryId())
                .orElseThrow(() -> new IllegalArgumentException("delivery is not found"));

        for (DeliveryItem d : delivery.getDeliveryItems()) {
            if (d.isDelivery_revoke()) {
                throw new IllegalStateException("some delivery item is revoke");
            }
            d.setDelivery_approval(true);
        }


        Order order = orderRepository.findById(deliveryRequestDTO.getOrderId())
                .orElseThrow(() -> new IllegalArgumentException("order not found"));

        for(OrderItem orderItem : order.getOrderItems()){
            if(orderItem.getItem().getMember().equals(member) ){
                if(orderItem.isDelivery_revoke()){
                    throw new IllegalStateException("some order item is revoke");

                }else orderItem.setDelivery_approval(true);
            }
        }

    }

    @Transactional
    public void approve_detail(Long orderItemId, String token) {
        String userId = tokenProvider.extractIdByAccessToken(token);
        Member member = memberRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new IllegalArgumentException("member not found"));
        OrderItem orderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> new IllegalArgumentException("order item not found"));
        if(orderItem.isDelivery_revoke()){
            throw new IllegalArgumentException("orderItem is revoke");
        }
        orderItem.setDelivery_approval(true);
        orderItemRepository.save(orderItem);

        DeliveryItem deliveryItem = deliveryItemRepository.findByOrderItemId(orderItemId);


        if(!member.getId().equals(deliveryItem.getSellerId())){
            throw new IllegalArgumentException("seller is not matching");
        }

        deliveryItem.setDelivery_approval(true);
        deliveryItemRepository.save(deliveryItem);

    }

    @Transactional
    public DeliveryRevokeResponse revoke(DeliveryRequestDTO deliveryRequestDTO, String token) {
        String userId = tokenProvider.extractIdByAccessToken(token);
        Member member = memberRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new IllegalArgumentException("member not found"));

        Delivery delivery = deliveryRepository.findById(deliveryRequestDTO.getDeliveryId())
                .orElseThrow(() -> new IllegalArgumentException("delivery is not found"));

        Order order = orderRepository.findById(deliveryRequestDTO.getOrderId())
                .orElseThrow(() -> new IllegalArgumentException("order not found"));

        for (DeliveryItem d : delivery.getDeliveryItems()) {
            d.setDelivery_revoke(true);
        }

        if(!member.getId().equals(delivery.getDeliveryItems().get(0).getSellerId())){
            throw new IllegalArgumentException("seller is not matching");
        }

        for(OrderItem orderItem : order.getOrderItems()){
            if(orderItem.getItem().getMember().equals(member) ){

                orderItem.setDelivery_revoke(true);
            }
        }
        // optionPrice 값을 추출

        return DeliveryRevokeResponse.builder()
                .tid(order.getTid())
                .itemName(delivery.getDeliveryItems().get(0).getItemName() + "외 " + (delivery.getDeliveryItems().size() -1) + "건")
                .itemQuantity(String.valueOf(delivery.getDeliveryItems().size()))
                .cancelAmount(
                        delivery.getDeliveryItems().stream()
                                .mapToLong(deliveryItem -> deliveryItem.getQuantity() * deliveryItem.getOptionPrice()) // optionPrice 값을 추출
                                .sum()
                )
                .build();

    }

    @Transactional
    public DeliveryRevokeResponse revoke_detail(DeliveryRevokeDTO deliveryRevokeDTO, String token) {
        String userId = tokenProvider.extractIdByAccessToken(token);
        Member member = memberRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new IllegalArgumentException("member not found"));

        OrderItem orderItem=null;

        int itemQuantity = 0;
        int cancelAmount = 0;

        for(Long orderItemId : deliveryRevokeDTO.getOrderItemIds()){
            orderItem = orderItemRepository.findById(orderItemId)
                    .orElseThrow(() -> new IllegalArgumentException("order item not found"));

            itemQuantity++;
            cancelAmount += orderItem.getOrder_price() * orderItem.getCount();

            orderItem.setDelivery_revoke(true);

            DeliveryItem deliveryItem = deliveryItemRepository.findByOrderItemId(orderItemId);

            if(!member.getId().equals(deliveryItem.getSellerId())){
                throw new IllegalArgumentException("seller is not matching");
            }

            deliveryItem.setDelivery_revoke(true);
        }
        String item_name = Objects.requireNonNull(orderItem).getOrder_name();

        if(!(itemQuantity==1)){
            item_name += "외 " + (itemQuantity -1) + "건";
        }

        return DeliveryRevokeResponse.builder()
                .tid(orderItem.getOrder().getTid())
                .itemQuantity(String.valueOf(itemQuantity))
                .cancelAmount((long) cancelAmount)
                .itemName(item_name)
                .build();
    }
    public void createDelivery(Member m, List<OrderItem> orderItems, Order order) {
        Delivery delivery = new Delivery(m, orderItems, order);
        deliveryRepository.save(delivery);
    }

}

