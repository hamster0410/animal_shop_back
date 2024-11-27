package animal_shop.shop.delivery.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DeliveryRevokeResponse {
    private String tid;
    private String itemName;
    private String itemQuantity;
    private Long cancelAmount;
    private int cancelTaxFreeAmount;
    private int cancelVatAmount;
}
