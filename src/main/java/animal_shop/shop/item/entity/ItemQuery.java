package animal_shop.shop.item.entity;

import animal_shop.community.member.entity.Member;
import animal_shop.global.dto.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class ItemQuery extends BaseTimeEntity {
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

    @ManyToOne
    @JoinColumn(name ="item_id", nullable = false)
    private Item item;

    private String contents;

    private String reply;

    private String option_name;

    private String option_price;
}
