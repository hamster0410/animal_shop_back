package animal_shop.shop.order_item.entity;

import animal_shop.global.dto.BaseTimeEntity;
import animal_shop.shop.item.entity.Item;
import animal_shop.shop.order.entity.Order;
import jakarta.persistence.*;

@Entity
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

    private int orderPrice;

    private int count;

}
