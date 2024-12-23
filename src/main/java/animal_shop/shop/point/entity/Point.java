package animal_shop.shop.point.entity;

import animal_shop.shop.point.PointStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
@Entity
public class Point {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "point_id")
    private Long id;

    private Long point;

    private Long sellerId;

    private Long buyerId;

    private Long itemId;

    private String itemName;

    private String optionName;

    private int quantity;

    private Long price;

    private LocalDateTime getDate;

    private Long deliveryCompletedId; // DeliveryCompleted와 간접 연관

    @Enumerated(EnumType.STRING)
    private PointStatus status = PointStatus.AVAILABLE;


}
