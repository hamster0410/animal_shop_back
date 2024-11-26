package animal_shop.shop.delivery.service;

import animal_shop.community.member.entity.Member;
import animal_shop.community.member.repository.MemberRepository;
import animal_shop.global.security.TokenProvider;
import animal_shop.shop.delivery.entity.Delivery;
import animal_shop.shop.delivery.repository.DeliveryRepository;
import animal_shop.shop.order.entity.Order;
import animal_shop.shop.order.repository.OrderRepository;
import animal_shop.shop.order_item.entity.OrderItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeliveryService {

    @Autowired
    TokenProvider tokenProvider;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    DeliveryRepository deliveryRepository;


    public void createDelivery(Member m, List<OrderItem> orderItems, Order order) {
        Delivery delivery = new Delivery(m, orderItems, order);
    }
}
