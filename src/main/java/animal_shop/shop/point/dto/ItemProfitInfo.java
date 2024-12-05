package animal_shop.shop.point.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class ItemProfitInfo {
    String itemName;
    List<PointProfitDTO> profitDTOList;

    public ItemProfitInfo(String itemName, List<PointProfitDTO> profitDTOList){
        this.itemName= itemName;
        this.profitDTOList = profitDTOList;

    }
}
