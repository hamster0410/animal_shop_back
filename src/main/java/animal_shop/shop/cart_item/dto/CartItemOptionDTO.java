package animal_shop.shop.cart_item.dto;

import animal_shop.shop.item.entity.Option;
import lombok.Getter;

@Getter
public class CartItemOptionDTO {

    private Long optionId;
    private String name;
    private long price;

    public CartItemOptionDTO(){

    }
    public CartItemOptionDTO(Option o) {
        this.optionId = o.getId();
        this.name = o.getName();
        this.price = o.getPrice();
    }
}
