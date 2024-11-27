package animal_shop.global.pay.entity;

import animal_shop.global.pay.dto.Amount;
import animal_shop.global.pay.dto.KakaoApproveResponse;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KakaoPay {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="kakao_pay_id")
    private Long id;

    private String aid;

    private String tid;

    private String cid;

    private String sid;

    private String partner_order_id;

    private String partner_user_id;

    @Embedded
    private Amount amount;

    private String item_name;

    private String item_code;

    private int quantity; // 상품 수량

    private String created_at; // 결제 요청 시간

    private String approved_at; // 결제 승인 시간

    private String payload; // 결제 승인 요청에 대해 저장 값, 요청 시 전달 내용

    public KakaoPay(KakaoApproveResponse response) {
        this.aid = response.getAid();
        this.tid = response.getTid();
        this.cid = response.getCid();
        this.sid = response.getSid();
        this.partner_order_id = response.getPartner_order_id();
        this.partner_user_id = response.getPartner_user_id();
        this.amount = response.getAmount();
        this.item_name = response.getItem_name();
        this.item_code = response.getItem_code();
        this.quantity = response.getQuantity();
        this.created_at = response.getCreated_at();
        this.approved_at = response.getApproved_at();
        this.payload = response.getPayload();
    }
}
