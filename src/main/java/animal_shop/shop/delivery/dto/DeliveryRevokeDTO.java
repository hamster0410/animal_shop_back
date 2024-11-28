package animal_shop.shop.delivery.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class DeliveryRevokeDTO {
    List<Long> orderItemIds;
}
