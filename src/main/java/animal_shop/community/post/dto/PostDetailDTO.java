package animal_shop.community.post.dto;

import animal_shop.community.post.entity.Post;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PostDetailDTO {
    private Long id;
    private String title;
    private String nickname;
    private String contents;
    private Long hits;
    private String category;
    private LocalDateTime createdDate;
    private Long count_comment;
    private Long count_heart;
    private boolean heart;

    public PostDetailDTO(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.nickname = post.getMember().getNickname();
        this.contents = post.getContents();
        this.hits = post.getHits();
        this.category = post.getCategory();
        this.createdDate = post.getCreatedDate();
        this.count_heart = post.getCountHeart();
        this.count_comment = post.getCountComment();
    }
}
