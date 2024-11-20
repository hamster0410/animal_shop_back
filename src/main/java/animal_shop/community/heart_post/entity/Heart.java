package animal_shop.community.heart_post.entity;

import animal_shop.community.member.entity.Member;
import animal_shop.community.post.entity.Post;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Table(name="HEART")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 기본 생성자 추가
public class Heart{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post; // 좋아요가 눌린 게시글

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member; // 좋아요를 누른 사용자

    @Builder
    public Heart(Member member, Post post){
        this.member = member;
        this.post = post;
    }

}