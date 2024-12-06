package animal_shop.shop.point.dto;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class MyPointDTO {
    String date;
    BigDecimal point;

    public MyPointDTO(Object [] obj){
       this.date = (String) obj[0];
       this.point = (BigDecimal) obj[1];
    }
}
