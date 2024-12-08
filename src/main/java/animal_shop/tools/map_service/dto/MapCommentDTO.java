package animal_shop.tools.map_service.dto;

import animal_shop.community.member.entity.Member;
import animal_shop.tools.map_service.entity.MapComment;
import animal_shop.tools.map_service.entity.MapEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MapCommentDTO {

    private Long id;

    private String contents;

    private long map_id;

    private List<String> map_comment_thumbnail_url;

    private Long rating;
//    private Long countHeart = 0L;
//    private boolean heart;

    public MapCommentDTO(MapComment mapComment){
        this.id = mapComment.getId();
        this.contents = mapComment.getContents();
        this.map_id = mapComment.getMap_id();
        this.map_comment_thumbnail_url = mapComment.getMap_comment_thumbnail_url();
        this.rating = mapComment.getRating();



    }





}
