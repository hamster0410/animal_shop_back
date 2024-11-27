package animal_shop.shop.delivery.dto;

import animal_shop.shop.delivery.entity.DeliveryItem;
import lombok.Getter;

@Getter
public class DeliveryItemDTO {
    private String itemName;

    private int quantity;

    private String optionName;

    private Long optionPrice;

    private Long sellerId;

    private Long buyerId;

    private Long orderItemId;

    public DeliveryItemDTO(DeliveryItem deliveryItem){
        this.itemName = deliveryItem.getItemName();
        this.quantity = deliveryItem.getQuantity();
        this.optionName = deliveryItem.getOptionName();
        this.optionPrice = deliveryItem.getOptionPrice();
        this.sellerId = deliveryItem.getSellerId();
        this.buyerId = deliveryItem.getBuyerId();
        this.orderItemId = deliveryItem.getOrderItemId();
    }
}
