package animal_shop.shop.order.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Builder
public class MyOrderCountDTO {
    private Long entire;
    private Long waiting;
    private Long approve;
    private Long revoke;
    private Long deliveryProgress;
    private Long deliveryCompleted;
}
