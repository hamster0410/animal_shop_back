package animal_shop.shop.order.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class OrderDTO {

    @Min(value = 1, message = "최소 주문 수량은 1개 입니다.")
    @Max(value = 999, message = "최대 주문 수량은 999개 입니다.")
    private int count;

    //option_id로 받지 않는 이유는 ?
    private String option_name;

    private int option_price;

    public OrderDTO(int count, String optionName, int optionPrice) {
        this.count = count;
        this.option_name = optionName;
        this.option_price = optionPrice;
    }
}
