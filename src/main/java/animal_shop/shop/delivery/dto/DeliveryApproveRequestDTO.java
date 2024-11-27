package animal_shop.shop.delivery.dto;

import lombok.Getter;

@Getter
public class DeliveryApproveRequestDTO {
    private Long orderId;
    private Long deliveryId;
}
