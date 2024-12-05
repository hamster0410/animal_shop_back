package animal_shop.shop.point.dto;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class PointProfitDTO {
    BigDecimal point;
    String optionName;

    public PointProfitDTO(Object[] obj){
        this.optionName = (String) obj[2];
        this.point = (BigDecimal) obj[3];

    }
}
