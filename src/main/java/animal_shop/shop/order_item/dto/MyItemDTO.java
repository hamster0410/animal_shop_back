package animal_shop.shop.order_item.dto;

import lombok.Getter;


@Getter
public class MyItemDTO {
    String date;
    long point;

    public MyItemDTO(Object [] obj){
        this.date = (String) obj[0];
        this.point = (long) obj[1];
    }
}
