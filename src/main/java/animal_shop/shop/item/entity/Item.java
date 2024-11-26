package animal_shop.shop.item.entity;


import animal_shop.community.member.entity.Member;
import animal_shop.global.dto.BaseTimeEntity;
import animal_shop.shop.item.ItemSellStatus;
import animal_shop.shop.item_comment.entity.ItemComment;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Item extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="item_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    private String itemDetail;

    // @OneToMany 관계로 수정
    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Option> options;

    // @OneToMany 관계로 수정
    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemComment> comments;


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

    public void removeStock(int stockNumber){
        Long restStock = this.stock_number - stockNumber;
        if(restStock<0){
            throw new RuntimeException("상품의 재고가 부족 합니다. (현재 재고 수량 :" + this.stock_number + ")");
        }this.stock_number = restStock;
    }

    public void addStock(int stockNumber){
        this.stock_number += stock_number;
    }


}


