package animal_shop.tools.abandoned_animal.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class InterestDTOResponse {
    private List<InterestAnimalDTO> interestAnimalDTOList;
    private long total_count;
}
