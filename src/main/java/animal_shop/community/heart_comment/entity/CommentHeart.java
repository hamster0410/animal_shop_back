package animal_shop.community.heart_comment.entity;

import animal_shop.community.comment.entity.Comment;
import animal_shop.community.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name="COMMENT_HEART")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 기본 생성자 추가
public class CommentHeart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "comment_id", nullable = false)
    private Comment comment; // 좋아요가 눌린 게시글

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member; // 좋아요를 누른 사용자

    @Builder
    public CommentHeart(Member member, Comment comment){
        this.member = member;
        this.comment =comment;
    }

}