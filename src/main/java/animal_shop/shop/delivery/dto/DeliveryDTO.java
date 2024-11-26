package animal_shop.shop.delivery.dto;

import animal_shop.shop.delivery.entity.Delivery;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class DeliveryDTO {

    private Long id;

    //판매자의 아이디
    private Long memberId;

    private String orderCode;

    private LocalDateTime orderDate;

    private Long totalPrice;

    public DeliveryDTO(Delivery delivery){
        this.id = delivery.getId();

        this.memberId = delivery.getMember().getId();

        this.orderCode = delivery.getOrderCode();

        this.orderDate = delivery.getOrderDate();

        this.totalPrice = delivery.getTotalPrice();
    }

}
