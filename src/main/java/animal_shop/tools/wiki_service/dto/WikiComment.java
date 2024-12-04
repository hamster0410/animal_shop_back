package animal_shop.tools.wiki_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WikiComment {

    private Long id;
    private String content;
    private LocalDateTime createdDate;
    //댓글을 달았을 때 wiki의 ID
    private Long wikiID;
}
