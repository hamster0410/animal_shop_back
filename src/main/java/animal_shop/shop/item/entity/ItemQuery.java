package animal_shop.shop.item.entity;

import animal_shop.community.member.entity.Member;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class ItemQuery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="item_query_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "seller_id", nullable = false)
    private Member seller;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Member customer;

    private String product;

    private String contents;

    private String reply;

    private String option_name;

    private String option_price;
}
