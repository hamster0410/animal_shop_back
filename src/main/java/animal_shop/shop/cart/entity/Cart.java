package animal_shop.shop.cart.entity;

import animal_shop.community.member.entity.Member;
import jakarta.persistence.*;

@Entity
public class Cart {

    @Id
    @Column(name="cart_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;

}
