package animal_shop.community.post.dto;

import animal_shop.community.post.entity.Post;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PostListDTO {
    private Long id;
    private String title;
    private Long hits;
    private String nickname;
    private String category;
    private Long count_comment;
    private Long count_heart;
    private LocalDateTime createdDate;

    public PostListDTO(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.hits = post.getHits();
        this.nickname = post.getMember().getNickname();
        this.category = post.getCategory();
        this.count_heart = post.getCountHeart();
        this.count_comment = post.getCountComment();
        this.createdDate = post.getCreatedDate();
    }
}
