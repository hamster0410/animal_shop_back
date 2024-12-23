package animal_shop.tools.abandoned_animal.dto;

import animal_shop.tools.abandoned_animal.entity.AbandonedComment;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class AbandonedCommentDTO {
    private Long id;
    private String content;
    private String author;
    private LocalDateTime createdDate;      // 생성 시간
    private LocalDateTime lastModifiedDate; // 수정 시간
    private Long abandoned_animal_id;

    public AbandonedCommentDTO(AbandonedComment abandonedComment) {
        this.id = abandonedComment.getId();
        this.content = abandonedComment.getContent();
        this.author = abandonedComment.getAuthor();
        this.createdDate = abandonedComment.getCreatedDate();
        this.lastModifiedDate = abandonedComment.getLastModifiedDate();
        this.abandoned_animal_id = abandonedComment.getAbandonedAnimal().getId();
    }
}

