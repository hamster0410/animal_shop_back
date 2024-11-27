package animal_shop.global.pay.dto;

import lombok.Getter;

@Getter
public class KakaoSuccessRequest {
    private String tid;
    private String pg_token;
    private String partner_order_id;
    private String partner_user_id;
}
