package animal_shop.shop.item.dto;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ItemDiscountDTO {

    private Long option_id;

    private Long option_discount_rate;

//    public ItemDiscountDTO(Item item){
//     this.id = item.getId();
//     this.discount_rate = item.getDiscount_rate();
//    }
}
