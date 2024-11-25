package animal_shop.shop.item_comment.entity;

import animal_shop.community.member.entity.Member;
import animal_shop.global.dto.BaseTimeEntity;
import animal_shop.shop.item.entity.Item;
import animal_shop.shop.item_comment_like.entity.ItemCommentLike;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 기본 생성자 추가
public class ItemComment extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="item_comment_id")
    private Long id;

    private String contents;

    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @OneToMany(mappedBy = "itemComment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemCommentLike> likes = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "item_comment_thumbnail_urls", joinColumns = @JoinColumn(name = "item_comment_id"))
    @Column(name = "thumbnail_url")
    private List<String> thumbnail_url;

    private Long rating;

    private Long countHeart;

}