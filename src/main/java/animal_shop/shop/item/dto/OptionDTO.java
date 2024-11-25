package animal_shop.shop.item.dto;

import lombok.Getter;

@Getter
public class OptionDTO {
    private String name;
    private Long price;
    private Long optionId;

    public OptionDTO(String name, Long price, Long optionId) {
        this.optionId = optionId;
        this.name = name;
        this.price = price;
    }

}
