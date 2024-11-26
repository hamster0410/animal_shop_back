package animal_shop.global.kakaopay.dto;

import lombok.Getter;

@Getter
public class KakaoSuccessRequest {
    private String tid;
    private String pg_token;
}
