package animal_shop.tools.wiki_service.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class WikiDTOResponse {
    List<WikiDTO> wikiDTOList;
    long total_count;
}
