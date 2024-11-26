package animal_shop.shop.delivery.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class DeliveryDTOResponse {
    private List<DeliveryDTO> deliveryDTOList;
    private Long total_count;
}
