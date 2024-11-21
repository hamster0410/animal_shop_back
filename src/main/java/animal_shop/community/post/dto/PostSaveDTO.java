package animal_shop.community.post.dto;

import animal_shop.community.post.entity.Post;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostSaveDTO {

    private Long id;
    private String title;
    private String contents;
    private Long hits;
    private String member;
    private String category;



    public PostSaveDTO(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.contents = post.getContents();
        this.hits = post.getHits();
        this.member = post.getMember().getUsername();
        this.category = post.getCategory();
    }


}
