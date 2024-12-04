package animal_shop.tools.abandoned_animal.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter @Builder
public class AnimalListDTOResponse {
    private List<AnimalListDTO> animalListDTOList;
    private long total_count;
}
