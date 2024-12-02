package animal_shop.shop.order_item.dto;

import animal_shop.community.member.entity.Member;
import animal_shop.shop.delivery.entity.DeliveryCompleted;
import animal_shop.shop.delivery.entity.DeliveryItem;
import animal_shop.shop.delivery.entity.DeliveryProgress;
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

    public OrderItemDTO(DeliveryProgress deliveryProgress,DeliveryItem deliveryItem, OrderItem orderItem) {
        this.itemId = deliveryProgress.getId();
        this.itemNm = deliveryItem.getItemName();
        this.count = deliveryItem.getQuantity();
        this.orderPrice = Math.toIntExact(deliveryItem.getOptionPrice());
        this.orderName = orderItem.getOrder_name();
        this.imgUrl = orderItem.getItem().getThumbnail_url().get(0);
    }

    public OrderItemDTO(DeliveryCompleted deliveryCompleted, DeliveryItem deliveryItem, OrderItem orderItem) {
        this.itemId = deliveryCompleted.getId();
        this.itemNm = deliveryItem.getItemName();
        this.count = deliveryItem.getQuantity();
        this.orderPrice = Math.toIntExact(deliveryItem.getOptionPrice());
        this.orderName = orderItem.getOrder_name();
        this.imgUrl = orderItem.getItem().getThumbnail_url().get(0);

    }

//    public OrderItemDTO();
}
