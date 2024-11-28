package animal_shop.global.pay.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class KakaoReadyRequest {
    private String partner_order_id;
    private String partner_user_id;
    private String item_name;
    private String quantity;
    private String total_amount;
    private String vat_amount;
    private String tax_free_amount;


    public KakaoReadyRequest(String orderCode, String nickname, String name, String quantity, String total_amount) {
        this.partner_order_id = orderCode;
        this.partner_user_id = nickname;
        this.item_name = name;
        this.quantity = quantity;
        this.total_amount = total_amount;
    }
}
