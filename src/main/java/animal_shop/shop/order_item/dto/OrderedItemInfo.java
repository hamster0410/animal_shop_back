package animal_shop.shop.order_item.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class OrderedItemInfo {
    String name;
    List<OrderedOptionInfo> orderedOptionInfoList;

    public OrderedItemInfo(String key, List<OrderedOptionInfo> orderedOptionInfos) {
        this.name = key;
        this.orderedOptionInfoList = orderedOptionInfos;
    }
}
