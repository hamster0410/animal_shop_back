package animal_shop.shop.item.dto;

import animal_shop.shop.item.entity.Item;
import animal_shop.shop.item.entity.Option;
import lombok.Data;

import java.util.List;

@Data
public class ItemDetailDTO {
   private Long id;

   private String name;

   private List<Option> options;

   private String seller;

   private String species;

   private String category;

   private Long comment_count;

   private List<String> thumbnail_url;

   private String image_url;


   public ItemDetailDTO(Item item) {
       this.id = item.getId();
       this.name = item.getName();
       this.options = item.getOptions();
       this.seller = item.getMember().getNickname();
       this.species = item.getSpecies();
       this.comment_count = item.getComment_count();
       this.thumbnail_url = item.getThumbnail_url();
       this.image_url = item.getImage_url();
   }
}
