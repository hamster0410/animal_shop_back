package animal_shop.shop.cart_item.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class CartItemDetailResponse {
    private Long cartItemId;

    private String cartItemImg;

    private String cartItemName;

    private List<CartItemOptionDTO> options;

    private Long total_count;
}
