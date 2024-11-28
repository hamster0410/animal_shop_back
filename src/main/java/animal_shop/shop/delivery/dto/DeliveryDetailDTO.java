package animal_shop.shop.delivery.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DeliveryDetailDTO {
    private String customer;
    private String thumbnailUrl;
    private String itemName;
    private String optionName;
    private long price;
    private long quantity;
}
