package animal_shop.shop.item.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class QueryResponse {
    List<ResponseItemQueryDTO> responseItemQueryDTOList;
    Long total_count;
}
