package animal_shop.shop.order.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class OrderDTOList {
    private Long itemId;
    private List<OrderDTO> option_items;
}
