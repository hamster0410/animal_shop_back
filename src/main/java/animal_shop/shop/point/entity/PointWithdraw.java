package animal_shop.shop.point.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
public class PointWithdraw {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "point_withdraw_id")
    private Long id;

    private Long totalWithdrawAmount;

    private Long sellerId;

    private LocalDateTime withdrawDate;

}
