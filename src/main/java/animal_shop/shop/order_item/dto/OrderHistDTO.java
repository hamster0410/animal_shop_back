package animal_shop.shop.order_item.dto;

import animal_shop.shop.order.OrderStatus;
import animal_shop.shop.order.entity.Order;
import animal_shop.shop.order_item.entity.OrderItem;
import lombok.Getter;
import lombok.Setter;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class OrderHistDTO {

    private Long orderId;

    private String orderDate;


    private OrderStatus orderStatus;

    //주문 상품 리스트
    private List<OrderItemDTO> orderItemDTOList = new ArrayList<>();

    public OrderHistDTO(Order order){
        this.orderId = order.getId();
        this.orderDate = order.getOrderDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        this.orderStatus = order.getOrderStatus();

    }

    //orderItemDTO객체를 주문 상품 리스트에 추가
    public void addOrderItemDTO(OrderItemDTO orderItemDTO){
        orderItemDTOList.add(orderItemDTO);
    }
}
