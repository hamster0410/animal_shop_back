package animal_shop.shop.delivery.dto;

import animal_shop.shop.delivery.entity.DeliveryItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryItemDTO {
    private String itemName;

    private int quantity;

    private String optionName;

    private Long optionPrice;

    private Long orderItemId;

    private boolean delivery_approval;

    private boolean delivery_revoke;

    private String thumbnailUrl;


    public DeliveryItemDTO(DeliveryItem deliveryItem){
        this.itemName = deliveryItem.getItemName();
        this.quantity = deliveryItem.getQuantity();
        this.optionName = deliveryItem.getOptionName();
        this.optionPrice = deliveryItem.getOptionPrice();
        this.orderItemId = deliveryItem.getOrderItemId();
        this.delivery_approval = deliveryItem.isDelivery_approval();
        this.delivery_revoke = deliveryItem.isDelivery_revoke();
    }
}
