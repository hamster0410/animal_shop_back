package animal_shop.shop.item_comment.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class RequsetItemCommentDTO {
    private String contents;
    private Long rating;
    private List<String> thumbnailUrls;  // 썸네일 URL 리스트
}
