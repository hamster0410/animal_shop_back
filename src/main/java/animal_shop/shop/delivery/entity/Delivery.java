package animal_shop.shop.delivery.entity;

import animal_shop.community.member.entity.Member;
import animal_shop.shop.order.entity.Order;
import animal_shop.shop.order_item.entity.OrderItem;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Delivery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "delivery_id")
    private Long id;

    //판매자의 아이디
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(nullable = false)
    private String orderCode;

    private LocalDateTime orderDate;

    private Long totalPrice;

    @OneToMany(mappedBy = "delivery", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DeliveryItem> deliveryItems = new ArrayList<>();

    public Delivery(Member m, List<OrderItem> orderItems, Order order) {
        this.orderCode = order.getOrderCode();
        this.orderDate = order.getOrderDate();
        long total_porice = 0L;
        for(OrderItem o : orderItems){
            total_porice += (long) o.getOrder_price() * o.getCount();
        }
        this.totalPrice = total_porice;
        this.member = m;
        this.deliveryItems = orderItems.stream()
                .map(orderItem -> {
                    DeliveryItem deliveryItem = new DeliveryItem();
                    deliveryItem.setItemName(orderItem.getItem().getName()); // OrderItem의 이름 복사
                    deliveryItem.setQuantity(orderItem.getCount()); // OrderItem의 수량 복사
                    deliveryItem.setDelivery(this); // 현재 Delivery 객체와 연관 설정
                    deliveryItem.setOptionName(orderItem.getOrder_name());
                    deliveryItem.setOptionPrice((long) orderItem.getOrder_price());
                    deliveryItem.setBuyerId(order.getMember().getId());
                    deliveryItem.setSellerId(m.getId());
                    return deliveryItem;
                }).toList();
    }
}
