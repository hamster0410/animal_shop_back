package animal_shop.shop.cart.dto;

import animal_shop.community.member.dto.DeliveryInfoDTO;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class CartDetailDTOResponse {
    private List<CartDetailDTO> cartDetailDTOList;
    private DeliveryInfoDTO deliveryInfoDTO;
    private Long total_count;

}
