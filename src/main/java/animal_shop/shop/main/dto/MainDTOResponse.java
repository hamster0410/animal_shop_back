package animal_shop.shop.main.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class MainDTOResponse {
    List<MainDTO> animal_new;
    List<MainDTO> animal_hot;
    List<MainDTO> animal_custom;
}
