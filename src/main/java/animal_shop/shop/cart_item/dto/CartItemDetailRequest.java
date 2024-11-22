package animal_shop.shop.cart_item.dto;

import animal_shop.shop.cart.dto.CartDetailDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
public class CartItemDetailRequest {
    private Long total_count;
    private List<CartDetailDTO> cartDetailDTOList;
}
