package animal_shop.shop.point.dto;

import lombok.Getter;

@Getter
public class PointTotalDTO {
    private String date;
    private Long point;

    public PointTotalDTO(Object[] obj) {
        this.date = String.valueOf(obj[0]);
        this.point = (Long) obj[1];
    }
}
