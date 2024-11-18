package animal_shop.shop.item.dto;

import animal_shop.shop.item.ItemSellStatus;
import animal_shop.shop.item.entity.Option;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class ItemDTOList {

    private List<Option> option;

    private String name;

    private String item_detail;

    private Long stock_number;

    private ItemSellStatus sell_status;

    private String species;

    private String category;

    private List<String> thumbnailUrls;  // 썸네일 URL 리스트

    private String imageUrl;
}