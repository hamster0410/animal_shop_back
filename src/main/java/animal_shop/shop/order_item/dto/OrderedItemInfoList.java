package animal_shop.shop.order_item.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Builder
public class OrderedItemInfoList {
    String date;
    List<OrderedItemInfo> orderedOptionInfoList;
    LocalDateTime first_date;

}
