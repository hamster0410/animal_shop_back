package animal_shop.shop.item.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class ItemDTOListResponse {
    private List<ItemDetailDTO> itemDTOLists;
    private Long total_count;
}
