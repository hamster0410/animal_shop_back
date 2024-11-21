package animal_shop.shop.item_comment.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ItemCommentDTOResponse {
    private List<ItemCommentDTO> comments;
    private long total_count;
}
