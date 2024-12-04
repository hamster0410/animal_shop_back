package animal_shop.global.admin.entity;

import animal_shop.community.member.entity.Member;
import animal_shop.global.admin.dto.StopItemDTO;
import animal_shop.shop.item.entity.Item;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class StopItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long stopItemId;

    private String suspensionReason;

    private Long adminId;

    private Long sellerId;

    private LocalDateTime stopDate;

    public StopItem(Item item, Member admin, Member seller, StopItemDTO stopItemDTO) {
        this.stopItemId = item.getId();
        this.suspensionReason = stopItemDTO.getSuspensionReason();
        this.adminId = admin.getId();
        this.sellerId = seller.getId();
        this.stopDate = LocalDateTime.now();
    }
}
