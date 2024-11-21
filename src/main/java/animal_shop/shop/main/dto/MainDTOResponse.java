package animal_shop.shop.main.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class MainDTOResponse {
    List<MainDTO> new_goods;
    List<MainDTO> dog_hot;
    List<MainDTO> cat_hot;
}
