package animal_shop.shop.cart.entity;

import animal_shop.community.member.entity.Member;
import animal_shop.global.dto.BaseTimeEntity;
import animal_shop.shop.cart_item.entity.CartItem;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
@Entity
public class Cart extends BaseTimeEntity {

    @Id
    @Column(name="cart_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> cartItems;

    public static Cart createCart(Member member){
        Cart cart = new Cart();
        cart.setMember(member);
        return cart;
    }

}
