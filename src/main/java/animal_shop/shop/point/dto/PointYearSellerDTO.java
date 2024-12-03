package animal_shop.shop.point.dto;

import lombok.Getter;

@Getter
public class PointYearSellerDTO {
    private String date;
    private Long point;
    private String sellerNickname;

    public PointYearSellerDTO(String date, long point, String sellerNickname){
        this.date =date;
        this.point = point;
        this.sellerNickname = sellerNickname;

    }
}
