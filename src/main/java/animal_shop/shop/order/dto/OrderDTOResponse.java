package animal_shop.shop.order.dto;

import animal_shop.shop.order.entity.Order;
import animal_shop.shop.order_item.entity.OrderItem;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class OrderDTOResponse {

    private Long partner_order_id;

    private String partner_user_id;

    private String item_name;

    private Long quantity = 0L;

    private LocalDateTime orderDate;

    private Long total_amount;

    public OrderDTOResponse(Order order){
        this.partner_order_id = order.getId();
        this.partner_user_id = order.getMember().getNickname();
        this.item_name = order.getOrderItems().get(0).getItem().getName();
        for(OrderItem orderItem : order.getOrderItems()){
            this.quantity += orderItem.getCount();
        }
        this.orderDate = order.getOrderDate();
        this.total_amount = (long) order.getTotalPrice();
    }

}
