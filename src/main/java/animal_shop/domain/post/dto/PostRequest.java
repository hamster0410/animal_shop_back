package animal_shop.domain.post.dto;

import animal_shop.domain.post.entity.Post;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostRequest {

    private String title;
    private String contents;
    private String category;

    public Post toEntity() {
        return Post.builder()
                .title(title)
                .contents(contents)
                .category(category)
                .build();
    }

}

