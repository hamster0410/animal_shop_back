package animal_shop.shop.main.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class MainDTOBestResponse {
    List<MainDTO> goods;
    Long total_count;

}
