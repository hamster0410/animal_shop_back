package animal_shop.community.member.entity;

import animal_shop.community.member.dto.DeliveryInfoDTO;
import jakarta.persistence.*;
import lombok.Setter;

@Entity
public class DeliveryInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="delivery_info_id")
    private Long id;

    @Setter
    private String recipient;

    private String phoneNumber;

    private String address;

    private String deliveryRequest;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member; // 댓글을 단 사용자

    public DeliveryInfo(DeliveryInfoDTO deliveryInfoDTO) {
    }
}
