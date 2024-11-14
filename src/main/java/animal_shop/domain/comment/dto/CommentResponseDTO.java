package animal_shop.domain.comment.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class CommentResponseDTO {
    private List<CommentDTO> comments;
    private long totalCommentCount;
}
