package animal_shop.shop.order_item.dto;

import animal_shop.shop.order_item.entity.OrderItem;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class OrderItemDTO {

    private Long itemId;

    private String itemNm;

    private int count;

    private int orderPrice;

    private String orderName;

    private String imgUrl;

    public OrderItemDTO(OrderItem orderItem, String imgUrl){
        this.itemId = orderItem.getItem().getId();
        this.itemNm = orderItem.getItem().getName();
        this.count = orderItem.getCount();
        this.orderPrice = orderItem.getOrder_price();
        this.orderName = orderItem.getOrder_name();
        this.imgUrl = imgUrl;
    }
}
