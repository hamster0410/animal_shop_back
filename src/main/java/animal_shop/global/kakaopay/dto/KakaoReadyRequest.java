package animal_shop.global.kakaopay.dto;

import lombok.Getter;

@Getter
public class KakaoReadyRequest {
    private String partner_order_id;
    private String partner_user_id;
    private String item_name;
    private String quantity;
    private String total_amount;
    private String vat_amount;
    private String tax_free_amount;
}
