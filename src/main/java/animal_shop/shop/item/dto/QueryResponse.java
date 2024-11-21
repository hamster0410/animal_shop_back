package animal_shop.shop.item.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class QueryResponse {
    List<ResponseItemQueryDTO> responseItemQueryDTOList;
    Long total_count;
}
