package animal_shop.global.pay.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "payments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Naverpayment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="order_id")
    private Long id;

    private String merchantPayKey;

    @Column(name="status")
    private String status;

    private String productName;

    private int productCount;

    private int totalPayAmount;

    private int taxScopeAmount;

    private int taxExScopeAmount;

    private String returnUrl;

    public void setStatus(String status) {
        this.status = status; // 상태를 저장하는 필드 추가 및 수정
    }
}

