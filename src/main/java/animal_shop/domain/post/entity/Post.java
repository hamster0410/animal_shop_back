package animal_shop.domain.post.entity;

import animal_shop.global.dto.BaseTimeEntity;

import animal_shop.domain.comment.entity.Comment;
import animal_shop.domain.heart_post.entity.Heart;
import animal_shop.domain.member.entity.Member;
import animal_shop.domain.post.dto.PostSaveDTO;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name="POST")
@AllArgsConstructor //생성자 자동생성
public class Post extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="post_id")
    private Long id;

    @Setter
    @Column(nullable = false)
    private String title;

    @Setter
    @Column(length = 3000)
    private String contents;

    @Setter
    @Column
    private Long hits;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Setter
    @Column
    private Long countHeart = 0L; // 기본값으로 0으로 초기화

    @Setter
    @Column
    private Long countComment = 0L; // 기본값으로 0으로 초기화


    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Heart> hearts = new ArrayList<>(); // 좋아요 리스트

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>(); // 좋아요 리스트

    @Setter
    @Column
    private String category;


    @Builder
    public Post(String title, String contents, Member member,String category,Long hits){
        this.title = title;
        this.contents = contents;
        this.member = member;
        this.category = category;
        this.hits = hits;
        this.countComment = 0L;
        this.countHeart = 0L;
    }

    public PostSaveDTO toDto() {
        return new PostSaveDTO(this);
    }

}