package animal_shop.shop.delivery.dto;

import animal_shop.community.member.entity.Member;
import animal_shop.shop.delivery.DeliveryStatus;
import animal_shop.shop.delivery.entity.DeliveryCompleted;
import animal_shop.shop.delivery.entity.DeliveryItem;
import animal_shop.shop.delivery.entity.DeliveryProgress;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class DeliveryCustomerDTO {

    private Long deliveryProgressId;

    private String seller;

    private String buyer;

    private String address;

    private String trackingNumber;

    private String itemName;

    private String optionName;

    private Long optionPrice;

    private long quantity;

    private Long totalPrice;

    //배송인 이름
    private String courier;

    private LocalDateTime delivery_start_date;

    private DeliveryStatus deliveryStatus;
    public DeliveryCustomerDTO(DeliveryProgress deliveryProgress,Member seller, Member buyer, DeliveryItem deliveryItem){
        this.deliveryProgressId = deliveryProgress.getId();
        this.buyer = buyer.getNickname();
        this.seller = seller.getNickname();
        this.address = deliveryProgress.getAddress();
        this.trackingNumber = deliveryProgress.getTrackingNumber();
        this.itemName = deliveryItem.getItemName();
        this.optionName = deliveryItem.getOptionName();
        this.optionPrice = deliveryItem.getOptionPrice();
        this.quantity = deliveryItem.getQuantity();
        this.totalPrice = this.optionPrice * this.quantity;
        this.courier = deliveryProgress.getCourier();
        this.delivery_start_date = deliveryProgress.getDeliveredDate();
        this.deliveryStatus = deliveryProgress.getDeliveryStatus();
    }

    public DeliveryCustomerDTO(DeliveryCompleted deliveryCompleted, Member seller, Member buyer, DeliveryItem deliveryItem){
        this.deliveryProgressId = deliveryCompleted.getId();
        this.buyer = buyer.getNickname();
        this.seller = seller.getNickname();
        this.address = deliveryCompleted.getAddress();
        this.trackingNumber = deliveryCompleted.getTrackingNumber();
        this.itemName = deliveryItem.getItemName();
        this.optionName = deliveryItem.getOptionName();
        this.optionPrice = deliveryItem.getOptionPrice();
        this.quantity = deliveryItem.getQuantity();
        this.totalPrice = this.optionPrice * this.quantity;
        this.courier = deliveryCompleted.getCourier();
        this.delivery_start_date = deliveryCompleted.getDeliveredDate();
    }
}
