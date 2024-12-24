package animal_shop.global.pay.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KakaoCancelRequest {
    private String tid;
    private String itemName;
    private String itemQuantity;
    private int cancelAmount;
    private int cancelTaxFreeAmount;
    private int cancelVatAmount;
}
