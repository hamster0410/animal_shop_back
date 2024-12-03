package animal_shop.shop.point.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Builder
public class PointTotalDTOResponse {
    List<PointTotalDTO> pointTotalDTOList;
    LocalDateTime first_date;
}
