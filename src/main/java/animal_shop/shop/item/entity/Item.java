package animal_shop.shop.item.entity;


import animal_shop.community.member.entity.Member;
import animal_shop.global.dto.BaseTimeEntity;
import animal_shop.shop.item.ItemSellStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Item extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="item_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    // @OneToMany 관계로 수정
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private List<Option> options;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false)
    private Long stock_number;

    @Column(nullable = false)
    private ItemSellStatus itemSellStatus;

    @Column(nullable = false, length = 20)
    private String species;

    @Column(nullable = false, length = 20)
    private String category;

    private Long comment_count;

    @ElementCollection
    @CollectionTable(name = "item_thumbnail_urls", joinColumns = @JoinColumn(name = "item_id"))
    @Column(name = "thumbnail_url")
    private List<String> thumbnail_url;

    private String image_url;


}


