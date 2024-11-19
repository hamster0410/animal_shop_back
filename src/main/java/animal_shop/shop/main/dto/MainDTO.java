package animal_shop.shop.main.dto;

import animal_shop.shop.item.entity.Item;
import lombok.Data;

@Data
public class MainDTO {
    private Long id;
    private String name;
    private String nickname;
    private String thumbnail_url;
    private Long price;
    private Long rating;
    private Long comment_count;

    public MainDTO(Item item){
        this.id = item.getId();
        this.name = item.getName();
        this.nickname = item.getMember().getNickname();
        this.thumbnail_url = item.getThumbnail_url().get(0);
        this.price = item.getOptions().get(0).getPrice();
        this.rating = 0L;
        this.comment_count = 0L;
    }
}
