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

    private List<ItemDetailDTO> content;  // 아이템 목록
    private long totalItems;  // 전체 아이템 수



}
