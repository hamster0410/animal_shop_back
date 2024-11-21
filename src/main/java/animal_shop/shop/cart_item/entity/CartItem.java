package animal_shop.shop.cart_item.entity;

import animal_shop.global.dto.BaseTimeEntity;
import animal_shop.shop.cart.entity.Cart;
import animal_shop.shop.item.entity.Item;
import animal_shop.shop.item.entity.Option;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter  @Setter
@Table(name = "cart_item")
public class CartItem extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "option_id")
    private Option option;

    private int count;

    public static CartItem createCartItem(Cart cart, Item item, int count, Option option){
        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setItem(item);
        cartItem.setCount(count);
        cartItem.setOption(option);
        return cartItem;
    }

    public void addCount(int count){
        this.count += count;
    }

    public void updateCount(int count){
        this.count = count;
    }
}
