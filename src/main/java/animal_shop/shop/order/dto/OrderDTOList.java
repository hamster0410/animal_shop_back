package animal_shop.shop.order.dto;

import animal_shop.community.member.dto.DeliveryInfoDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class OrderDTOList {
    private Long itemId;
    private DeliveryInfoDTO deliveryInfoDTO;
    private List<OrderDTO> option_items;
}
