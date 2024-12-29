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

   private List<OptionDTO> options;

   private String item_detail;

   private String seller;

   private String species;

   private String category;

   private String detailed_category;

   private Long comment_count;

    private Long rating;

    private List<String> thumbnail_url;

   private String image_url;

   private ItemSellStatus sell_status;

   private Long stock_number;

   private String suspensionReason;

   private boolean now_discount;

   public ItemDetailDTO(Item item) {
       this.id = item.getId();
       this.name = item.getName();
       List<OptionDTO> list = new ArrayList<>();
       for (Option option : item.getOptions()) {
           OptionDTO optionDTO = new OptionDTO(option);
           list.add(optionDTO);
       }
       this.sell_status = item.getItemSellStatus();
       this.stock_number = item.getStock_number();
       this.item_detail = item.getItemDetail();
       this.options = list;
       long ar = 0L;
       if(item.getComment_count() != 0){
           ar = item.getTotal_rating()/item.getComment_count();
       }
       this.rating = ar;
       this.category = item.getCategory();
       this.detailed_category = item.getDetailed_category();
       this.seller = item.getMember().getNickname();
       this.species = item.getSpecies();
       this.comment_count = item.getComment_count();
       this.thumbnail_url = item.getThumbnail_url();
       this.image_url = item.getImage_url();
       this.now_discount = false;
       for(Option o : item.getOptions()){
           if(o.getDiscount_rate() > 0){
               now_discount = true;
           }
       }
   }

    public ItemDetailDTO(Item item, String suspensionReason) {
        this.id = item.getId();
        this.name = item.getName();
        List<OptionDTO> list = new ArrayList<>();
        for (Option option : item.getOptions()) {
            OptionDTO optionDTO = new OptionDTO(option);
            list.add(optionDTO);
        }
        long ar = 0L;
        if(item.getComment_count() != 0){
            ar = item.getTotal_rating()/item.getComment_count();
        }
        this.rating = ar;
        this.sell_status = item.getItemSellStatus();
        this.stock_number = item.getStock_number();
        this.item_detail = item.getItemDetail();
        this.options = list;
        this.category = item.getCategory();
        this.detailed_category = item.getDetailed_category();
        this.seller = item.getMember().getNickname();
        this.species = item.getSpecies();
        this.comment_count = item.getComment_count();
        this.thumbnail_url = item.getThumbnail_url();
        this.image_url = item.getImage_url();
        this.suspensionReason = suspensionReason;
    }
}
