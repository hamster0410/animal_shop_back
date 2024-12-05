package animal_shop.shop.order_item.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class OrderedOptionInfo {
    String option_name;
    Long count;

    public OrderedOptionInfo(Object[] obj){
        this.option_name = (String) obj[1];
        this.count = (Long) obj[3];

    }
}
