package animal_shop.tools.map_service.dto;

import animal_shop.tools.map_service.entity.MapComment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MapCommentDTO {

    private Long id;

    private String nickname;

    private String contents;

    private long map_id;

    private List<String> map_comment_thumbnail_url;

    private Long rating;
//    private Long countHeart = 0L;
//    private boolean heart;

    public MapCommentDTO(MapComment mapComment,String nickname){
        this.id = mapComment.getId();
        this.nickname=nickname;
        this.contents = mapComment.getContents();
        this.map_id = mapComment.getMapId();
        this.map_comment_thumbnail_url = mapComment.getMap_comment_thumbnail_url();
        this.rating = mapComment.getRating();

    }





}
