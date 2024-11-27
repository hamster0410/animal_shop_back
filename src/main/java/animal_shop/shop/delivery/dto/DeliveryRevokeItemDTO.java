package animal_shop.shop.delivery.dto;

import lombok.Getter;

@Getter
public class DeliveryRevokeItemDTO {
    private Long orderItemId;
    private String tid;
}
