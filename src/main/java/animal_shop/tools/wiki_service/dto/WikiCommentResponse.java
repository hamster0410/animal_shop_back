package animal_shop.tools.wiki_service.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class WikiCommentResponse {
    List<WikiCommentDTO> wikiCommentDTOList;
    long total_count;
}
