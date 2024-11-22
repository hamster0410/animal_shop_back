package animal_shop.shop.cart.dto;

import animal_shop.shop.cart_item.entity.CartItem;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CartDetailDTO {

    private Long cartItemId;

    private String itemNm;

    private int count;

    private String option_name;

    private Long option_price;

    private String imgUrl;

    public CartDetailDTO() {}

    public CartDetailDTO(CartItem cartItem){
        this.cartItemId = cartItem.getId();
        this.itemNm = cartItem.getItem().getName();
        this.count = cartItem.getCount();
        this.option_name = cartItem.getOption().getName();
        this.option_price = cartItem.getOption().getPrice();
        this.imgUrl = cartItem.getItem().getThumbnail_url().get(0);
    }

}
