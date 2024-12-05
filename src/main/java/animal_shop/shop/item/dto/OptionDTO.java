package animal_shop.shop.item.dto;

import animal_shop.shop.item.entity.Option;
import lombok.Getter;

@Getter
public class OptionDTO {
    private String name;
    private Long price;
    private Long optionId;
    private Long discountRate;

    public OptionDTO(Option option) {
        this.optionId = option.getId();
        this.name = option.getName();
        this.price = option.getPrice();
        this.discountRate = option.getDiscount_rate();
    }

}
