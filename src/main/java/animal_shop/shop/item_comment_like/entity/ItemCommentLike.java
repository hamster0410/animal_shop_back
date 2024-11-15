package animal_shop.shop.item_comment_like.entity;

import animal_shop.community.member.entity.Member;
import animal_shop.community.post.entity.Post;
import animal_shop.shop.item_comment.entity.ItemComment;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 기본 생성자 추가
public class ItemCommentLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "item_comment_id", nullable = false)
    private ItemComment itemComment;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member; // 좋아요를 누른 사용자

    private LocalDateTime createdAt;
}