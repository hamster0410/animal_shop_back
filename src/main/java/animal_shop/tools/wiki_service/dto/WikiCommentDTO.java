package animal_shop.tools.wiki_service.dto;

import animal_shop.tools.wiki_service.entity.WikiComment;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
public class WikiCommentDTO {
    private Long id;
    private String content;
    private String author;

    public WikiCommentDTO(WikiComment comment) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.author = comment.getAuthor();
    }
}
