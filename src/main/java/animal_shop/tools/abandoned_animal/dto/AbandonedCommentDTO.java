package animal_shop.tools.abandoned_animal.dto;

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
}

