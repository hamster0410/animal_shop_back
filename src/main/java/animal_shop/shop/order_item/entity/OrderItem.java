package animal_shop.shop.order_item.entity;

import animal_shop.global.dto.BaseTimeEntity;
import animal_shop.shop.item.entity.Item;
import animal_shop.shop.order.dto.OrderDTO;
import animal_shop.shop.order.entity.Order;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class OrderItem extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private int order_price;

    private String order_name;

    private int count;

    public static OrderItem createOrderItem(Item item, OrderDTO orderDTO){
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setCount(orderDTO.getCount());
        orderItem.setOrder_price(orderDTO.getOption_price());
        orderItem.setOrder_name(orderDTO.getOption_name());
        item.removeStock(orderDTO.getCount());
        return orderItem;
    }

    public int getTotalPrice(){
        return order_price*count;
    }

    public void cancel(){
        this.getItem().addStock(count);
    }
}
