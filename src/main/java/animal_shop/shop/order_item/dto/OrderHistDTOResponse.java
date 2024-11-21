package animal_shop.shop.order_item.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class OrderHistDTOResponse {

    List<OrderHistDTO> orderHistDTOList;
    Long total_count;
}
