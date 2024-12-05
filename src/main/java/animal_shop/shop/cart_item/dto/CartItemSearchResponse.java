package animal_shop.shop.cart_item.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class CartItemSearchResponse {
     List<CartItemSearchDTO> cartItemSearchDTOList;
}
