package animal_shop.community.post.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PostResponseDTO {
    private List<PostListDTO> posts;
    private long totalCount;
}
