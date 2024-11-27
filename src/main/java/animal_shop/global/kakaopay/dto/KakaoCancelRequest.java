package animal_shop.global.kakaopay.dto;

import lombok.Getter;

@Getter
public class KakaoCancelRequest {
    private String tid;
    private Long orderItemId;
    private int cancelAmount;
    private int cancelTaxFreeAmount;
    private int cancelVatAmount;
}
