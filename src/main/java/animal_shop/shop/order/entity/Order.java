package animal_shop.shop.order.entity;

import animal_shop.community.member.dto.DeliveryInfoDTO;
import animal_shop.community.member.entity.Member;
import animal_shop.shop.order.OrderStatus;
import animal_shop.shop.order.PaymentStatus;
import animal_shop.shop.order_item.entity.OrderItem;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "orders")
public class Order{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private LocalDateTime orderDate;

    private String tid;

    private Long totalPrice;

    private String orderCode;

    @Setter
    private String recipient;

    private String phoneNumber;

    private String address;

    private String deliveryRequest;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL
            , orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OrderItem> orderItems = new ArrayList<>();

    public void addOrderItem(OrderItem orderItem){
        //주문에 주문 아이템을 저장
        orderItems.add(orderItem);
        //주문아이템에 주문을 저장
        orderItem.setOrder(this);
    }

    public static Order createOrder(Member member, List<OrderItem> orderItemList, DeliveryInfoDTO deliveryInfoDTO){
        Order order = new Order();
        order.setMember(member);
        Long totalPrice = 0L;
        for(OrderItem orderItem : orderItemList){
            totalPrice += (long) orderItem.getOrder_price() * orderItem.getCount();
            order.addOrderItem(orderItem);
        }
        order.setTotalPrice(totalPrice);
        order.setOrderStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        order.setRecipient(deliveryInfoDTO.getRecipient());
        order.setPhoneNumber(deliveryInfoDTO.getPhoneNumber());
        order.setDeliveryRequest(deliveryInfoDTO.getDeliveryRequest());
        order.setAddress(deliveryInfoDTO.getAddress());
        return order;
    }

    public int getTotalPrice(){
        int totalPrice = 0;
        for(OrderItem orderItem : orderItems){
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
    }

    public void cancelOrder(){
        this.orderStatus = OrderStatus.CANCEL;

        for(OrderItem orderItem : orderItems){
            orderItem.cancel();
        }
    }

    public void paySuccess() {
        for(OrderItem orderitem : orderItems){
            orderitem.setPaymentStatus(PaymentStatus.valueOf("COMPLETED"));
        }
    }

    public void payFailure() {
        for(OrderItem orderitem : orderItems){
            orderitem.setPaymentStatus(PaymentStatus.valueOf("FAILED"));
        }
    }
}