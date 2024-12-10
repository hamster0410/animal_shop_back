package animal_shop.tools.map_service.dto;

import lombok.Data;

import java.util.List;

@Data

public class MapCommentDTOResponse {
    private List<MapCommentDTO> comments;
    private long total_count;
}
