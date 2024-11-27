package animal_shop.shop.delivery.dto;

import animal_shop.shop.delivery.entity.Delivery;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class DeliveryDTO {

    private Long id;

    //판매자의 아이디
    private Long buyerId;

    private Long orderId;

    private LocalDateTime orderDate;

    private Long totalPrice;

    private List<DeliveryItemDTO> deliveryItemDTOList;

    public DeliveryDTO(Delivery delivery){
        this.id = delivery.getId();

        this.buyerId = delivery.getDeliveryItems().get(0).getBuyerId();

        this.orderId = delivery.getOrderId();

        this.orderDate = delivery.getOrderDate();

        this.totalPrice = delivery.getTotalPrice();

        this.deliveryItemDTOList = delivery.getDeliveryItems().stream().map(DeliveryItemDTO::new).toList();
    }

}
