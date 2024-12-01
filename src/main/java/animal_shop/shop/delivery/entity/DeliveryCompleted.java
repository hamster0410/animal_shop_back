package animal_shop.shop.delivery.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
public class DeliveryCompleted {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "delivery_completed_id")
    private Long id;

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

    private LocalDateTime deliveredDate;


}
