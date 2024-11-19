package animal_shop.shop.item.dto;

import lombok.Getter;

@Getter
public class OptionDTO {
    private String name;
    private long price;

    public OptionDTO(String name, Long price) {
        this.name = name;
        this.price = price;
    }
}
