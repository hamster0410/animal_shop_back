package animal_shop.shop.delivery.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryDTO {

    private Long id;

    //판매자의 아이디
    private String customer;

    private Long orderId;

    private String tid;

    private LocalDateTime orderDate;

    private Long totalPrice;

    private List<DeliveryItemDTO> deliveryItemDTOList;



}
