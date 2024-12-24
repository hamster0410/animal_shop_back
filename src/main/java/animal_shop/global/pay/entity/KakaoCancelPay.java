package animal_shop.global.pay.entity;

import animal_shop.global.pay.dto.KakaoCancelResponse;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "kakao_payment_cancellation")
public class KakaoCancelPay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 기본 키

    @Column(nullable = false, length = 50)
    private String aid; // 요청 고유 ID

    @Column(nullable = false, length = 50)
    private String tid; // 결제 고유 ID

    @Column(nullable = false, length = 50)
    private String cid; // 가맹점 코드

    @Column(nullable = false, length = 20)
    private String status; // 상태 (예: CANCEL_PAYMENT)

    @Column(name = "partner_order_id", nullable = false, length = 50)
    private String partnerOrderId; // 가맹점 주문번호

    @Column(name = "partner_user_id", nullable = false, length = 50)
    private String partnerUserId; // 가맹점 사용자 ID

    @Column(name = "payment_method_type", nullable = false, length = 20)
    private String paymentMethodType; // 결제 수단

    @Embedded
    private animal_shop.global.pay.dto.Amount amount; // 금액 정보 (임베디드 객체)

    private Long approvedCancelAmount; // 승인된 취소 금액

    private Long canceledAmount; // 취소된 금액

    @Column(name = "item_name", nullable = false, length = 100)
    private String itemName; // 아이템 이름

    @Column(name = "item_code", length = 50)
    private String itemCode; // 아이템 코드 (nullable)

    @Column(nullable = false)
    private Integer quantity; // 수량

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt; // 생성 시간

    @Column(name = "approved_at")
    private LocalDateTime approvedAt; // 승인 시간

    @Column(name = "canceled_at")
    private LocalDateTime canceledAt; // 취소 시간

    @Column(length = 500)
    private String payload; // 추가 데이터 (nullable)

    // 생성자에서 KakaoCancelResponse body를 매핑
    public KakaoCancelPay(KakaoCancelResponse body) {
        this.aid = body.getAid();
        this.tid = body.getTid();
        this.cid = body.getCid();
        this.status = body.getStatus();
        this.partnerOrderId = body.getPartner_order_id();
        this.partnerUserId = body.getPartner_user_id();
        this.paymentMethodType = body.getPayment_method_type();
        this.amount = body.getAmount();
        this.approvedCancelAmount = (long) body.getApproved_cancel_amount().getTotal();
        this.canceledAmount = (long) body.getCanceled_amount().getTotal();
        this.itemName = body.getItem_name();
        this.itemCode = body.getItem_code();
        this.quantity = body.getQuantity();
        this.createdAt = LocalDateTime.parse(body.getCreated_at());
        this.approvedAt = body.getApproved_at() != null ? LocalDateTime.parse(body.getApproved_at()) : null;
        this.canceledAt = body.getCanceled_at() != null ? LocalDateTime.parse(body.getCanceled_at()) : null;
        this.payload = body.getPayload();
    }

}
