
package animal_shop.shop.item_comment.dto;

import animal_shop.shop.item_comment.entity.ItemComment;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ItemCommentDTO {

    private Long id;
    private String contents; // 댓글 내용
    private String nickname; // 댓글을 단 사용자
    private LocalDateTime createdDate;
    private Long rating;
    private List<String> thumbnailUrl;
    private Long countHeart = 0L; // 기본값으로 0으로 초기화
    private boolean heart;



    // 기본 생성자 추가
    public ItemCommentDTO() {}

    public ItemCommentDTO(ItemComment comment) {
        this.id = comment.getId();
        this.contents = comment.getContents();
        this.nickname = comment.getMember().getNickname();
        this.createdDate = comment.getCreatedDate();
        this.rating = comment.getRating();
        this.countHeart = comment.getCountHeart();
        this.thumbnailUrl = comment.getThumbnail_url();
    }
}
