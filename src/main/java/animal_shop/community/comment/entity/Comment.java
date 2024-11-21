package animal_shop.community.comment.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import animal_shop.global.dto.BaseTimeEntity;
import animal_shop.community.member.entity.Member;
import animal_shop.community.post.entity.Post;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "COMMENTS")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Setter
    private String content; // 댓글 내용

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post; // 댓글이 달린 게시글

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member; // 댓글을 단 사용자

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "parent_id")
    private Comment parent; // 대댓글의 부모 댓글

    @Setter
    @Column
    private Long countHeart; // 기본값으로 0으로 초기화

    @Setter
    private List<String> imageUrl;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference // 직렬화의 시작점에 추가
    private List<Comment> comments; // 대댓글 리스트

}
