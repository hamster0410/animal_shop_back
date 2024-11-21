package animal_shop.shop.cart.dto;

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

    private CartDetailDTO(Long cartItemId, String itemNm, int count){
        this.cartItemId = cartItemId;
        this.itemNm = itemNm;
        this.count = count;
    }

}
