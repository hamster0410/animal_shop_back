package animal_shop.shop.cart_item.dto;

import lombok.Getter;

@Getter
public class CartItemSearchDTO {
    private String date;
    private Long count;
    private String itemName;
    public CartItemSearchDTO(Object[] objects, Integer year, Integer month){
        this.date = (year != null ? year : "*") + "-" + (month != null ? month : "*");
        this.count = (Long) objects[1];
        this.itemName = (String) objects[0];
    }

}
