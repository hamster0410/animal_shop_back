package animal_shop.shop.main.dto;

import animal_shop.shop.item.entity.Item;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MainDTO {
    private  String species;
    private Long id;
    private String name;
    private String nickname;
    private String thumbnail_url;
    private Long price;
    private Long rating;
    private Long comment_count;
    private long option_count;
    private String category;
    private String detailed_category;
    private String suspensionReason;

    public MainDTO(Item item){
        this.id = item.getId();
        this.name = item.getName();
        this.nickname = item.getMember().getNickname();
        this.thumbnail_url = item.getThumbnail_url().get(0);
        this.price = item.getOptions().get(0).getPrice();
        long ar = 0L;
        if(item.getComment_count() != 0){
            ar = item.getTotal_rating()/item.getComment_count();
        }
        this.rating = ar;
        this.comment_count = item.getComment_count();
        this.option_count = item.getOptions().size();
        this.category = item.getCategory();
        this.detailed_category = item.getDetailed_category();
        this.species = item.getSpecies();
    }
    public MainDTO(Item item, String suspensionReason){
        this.id = item.getId();
        this.name = item.getName();
        this.nickname = item.getMember().getNickname();
        this.thumbnail_url = item.getThumbnail_url().get(0);
        this.price = item.getOptions().get(0).getPrice();
        long ar = 0L;
        if(item.getComment_count() != 0){
            ar = item.getTotal_rating()/item.getComment_count();
        }
        this.rating = ar;
        this.comment_count = item.getComment_count();
        this.option_count = item.getOptions().size();
        this.category = item.getCategory();
        this.detailed_category = item.getDetailed_category();
        this.species = item.getSpecies();
        this.suspensionReason = suspensionReason;
    }
}
