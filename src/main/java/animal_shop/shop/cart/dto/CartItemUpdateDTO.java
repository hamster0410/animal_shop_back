package animal_shop.shop.cart.dto;

import lombok.Getter;

@Getter
public class CartItemUpdateDTO {
    private Long cartItemId;
    private Long optionId;
    private int count;
}
