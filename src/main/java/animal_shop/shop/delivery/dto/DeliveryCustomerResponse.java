package animal_shop.shop.delivery.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter @Builder
public class DeliveryCustomerResponse {
    private List<DeliveryCustomerDTO> deliveryCustomerDTOList;
    private long total_count;
}
