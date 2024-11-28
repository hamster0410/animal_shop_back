package animal_shop.shop.order_item.dto;

import animal_shop.shop.order_item.entity.OrderItem;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class OrderItemDTO {

    private Long itemId;

    private Long orderItemId;

    private String itemNm;

    private int count;

    private int orderPrice;

    private String orderName;

    private String imgUrl;

    private boolean delivery_approval;

    private boolean delivery_revoke;

    public OrderItemDTO(OrderItem orderItem, String imgUrl){
        this.itemId = orderItem.getItem().getId();
        this.itemNm = orderItem.getItem().getName();
        this.orderItemId = orderItem.getId();
        this.count = orderItem.getCount();
        this.orderPrice = orderItem.getOrder_price();
        this.delivery_approval = orderItem.isDelivery_approval();
        this.delivery_revoke = orderItem.isDelivery_revoke();
        this.orderName = orderItem.getOrder_name();
        this.imgUrl = imgUrl;
    }
}
