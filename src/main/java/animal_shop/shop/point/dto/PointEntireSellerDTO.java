package animal_shop.shop.point.dto;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class PointEntireSellerDTO {
    private String date;
    private BigDecimal point;
    private String sellerNickname;

    public PointEntireSellerDTO(String date, BigDecimal point, String sellerNickname){
        this.date =date;
        this.point = point;
        this.sellerNickname = sellerNickname;

    }
}
