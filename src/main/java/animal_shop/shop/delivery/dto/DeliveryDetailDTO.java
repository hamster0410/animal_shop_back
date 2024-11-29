package animal_shop.shop.delivery.dto;

import animal_shop.shop.order.PaymentStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class DeliveryDetailDTO {
    private String recipient;
    private String thumbnailUrl;
    private String itemName;
    private String optionName;
    private String address;
    private String order_code;
    private LocalDateTime order_date;
    private PaymentStatus order_status;
    private String phone_number;
    private long total_price;
    private long quantity;
}
