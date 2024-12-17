package animal_shop.global.pay.dto;

import animal_shop.shop.order.entity.Order;
import animal_shop.shop.order_item.entity.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NaverpaymentDTO {
    private String merchantPayKey;
    private String productName;
    private int productCount;
    private int totalPayAmount;
    private int taxScopeAmount;
    private int taxExScopeAmount;
    private String returnUrl;

    public NaverpaymentDTO(Order order) {
        this.merchantPayKey = order.getOrderCode();
        this.productName = order.getOrderItems().get(0).getOrder_name();
        for(OrderItem orderItem : order.getOrderItems()){
            this.productCount += orderItem.getCount();
        }
        this.totalPayAmount = order.getTotalPrice();
        this.taxScopeAmount = 0;
        this.taxExScopeAmount = 0;
        this.returnUrl = "localhost:3000/";
    }
}