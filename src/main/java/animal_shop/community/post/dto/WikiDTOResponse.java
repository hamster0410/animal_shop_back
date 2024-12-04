package animal_shop.community.post.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class WikiDTOResponse {
    List<WikiDTO> wikiDTOList;
    long total_count;
}
