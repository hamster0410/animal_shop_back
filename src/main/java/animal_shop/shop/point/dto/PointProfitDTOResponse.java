package animal_shop.shop.point.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class PointProfitDTOResponse {
    List<PointProfitDTO> pointProfitDTOList;
    LocalDateTime first_date;
}
