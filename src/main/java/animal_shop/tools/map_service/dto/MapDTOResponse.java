package animal_shop.tools.map_service.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Builder
@Data
public class MapDTOResponse {
    private List<MapDTO> MapDTOList;
    private long total_count;
}
