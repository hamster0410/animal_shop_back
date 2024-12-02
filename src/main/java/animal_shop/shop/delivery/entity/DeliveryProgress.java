package animal_shop.shop.delivery.entity;

import animal_shop.shop.delivery.DeliveryStatus;
import animal_shop.shop.order_item.entity.OrderItem;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryProgress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "delivery_progress_id")
    private Long id;

    private Long orderId;

    @Column(nullable = false)
    private Long deliveryItemId;

    @Column(nullable = false)
    private Long sellerId;

    @Column(nullable = false)
    private Long buyerId;

    private String address;

    private String trackingNumber;

    //배송인 이름
    @Column(nullable = false,length = 30)
    private String courier;

    @Column(nullable = false)
    private LocalDateTime deliveredDate;

    private DeliveryStatus deliveryStatus;


    public DeliveryProgress(DeliveryItem deliveryItem, OrderItem orderItem){
        this.deliveryItemId = deliveryItem.getId();
        this.sellerId = deliveryItem.getSellerId();
        this.buyerId = deliveryItem.getBuyerId();
        this.address = deliveryItem.getDelivery().getAddress();
        this.trackingNumber = System.currentTimeMillis()+"-" + deliveryItem.getId();
        this.courier ="animalping";
        this.orderId = orderItem.getOrder().getId();
        this.deliveredDate = LocalDateTime.now();
        this.deliveryStatus = DeliveryStatus.COMPLETED;
    }

}
