package animal_shop.shop.point.dto;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class PointProfitDTO {
    String date;
    BigDecimal point;
    String itemName;

    public PointProfitDTO(Object[] obj){
        this.date = (String) obj[0];
        this.itemName = (String) obj[1];
        this.point = (BigDecimal) obj[2];

    }
}
