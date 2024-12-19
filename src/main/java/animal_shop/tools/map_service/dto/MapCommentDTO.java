package animal_shop.tools.map_service.dto;

import animal_shop.global.dto.BaseTimeEntity;
import animal_shop.tools.map_service.entity.MapComment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
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

    private LocalDateTime created_date;

//    private Long countHeart = 0L;
//    private boolean heart;

    public MapCommentDTO(MapComment mapComment){
        this.id = mapComment.getId();
        this.nickname=mapComment.getMember().getNickname();
        this.contents = mapComment.getContents();
        this.map_id = mapComment.getMapId();
        this.map_comment_thumbnail_url = mapComment.getMap_comment_thumbnail_url();
        this.rating = mapComment.getRating();
        this.created_date = mapComment.getCreatedDate();
    }

}
