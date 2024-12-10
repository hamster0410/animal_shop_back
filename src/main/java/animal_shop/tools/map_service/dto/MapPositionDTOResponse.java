package animal_shop.tools.map_service.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class MapPositionDTOResponse {
    List<MapPositionDTO> mapPositionDTOList;
    long total_count;
}
