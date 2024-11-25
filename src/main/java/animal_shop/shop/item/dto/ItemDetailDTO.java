package animal_shop.shop.item.dto;

import animal_shop.shop.item.ItemSellStatus;
import animal_shop.shop.item.entity.Item;
import animal_shop.shop.item.entity.Option;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ItemDetailDTO {
   private Long id;

   private String name;

   private Long itemId;

   private List<OptionDTO> options;

   private String item_detail;

   private String seller;

   private String species;

   private String category;

   private Long comment_count;

   private List<String> thumbnail_url;

   private String image_url;

   private ItemSellStatus sell_status;

   private Long stock_number;


   public ItemDetailDTO(Item item) {
       this.id = item.getId();
       this.name = item.getName();
       this.itemId = item.getId();
       List<OptionDTO> list = new ArrayList<>();
       for (Option option : item.getOptions()) {
           OptionDTO optionDTO = new OptionDTO(option.getName(), option.getPrice());
           list.add(optionDTO);
       }
       this.sell_status = item.getItemSellStatus();
       this.stock_number = item.getStock_number();
       this.item_detail = item.getItemDetail();
       this.options = list;
       this.category = item.getCategory();
       this.seller = item.getMember().getNickname();
       this.species = item.getSpecies();
       this.comment_count = item.getComment_count();
       this.thumbnail_url = item.getThumbnail_url();
       this.image_url = item.getImage_url();
   }
}
