package animal_shop.community.post.dto;

import animal_shop.community.post.entity.Notices;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Data

public class NoticesDTO {
    private Long id;
    private String title; // 공지사항 제목
    private String content; // 공지사항 내용
    private Integer priority; // 우선순위
    private LocalDateTime created_date;
    private String attachmentUrl;
    // 생성자: Notices 엔티티에서 NoticesDTO로 변환
//    public NoticesDTO(String title, String content, Integer priority) {
//        this.title = title;
//        this.content = content;
//        this.priority = priority;
//    }
    // Notices 엔티티를 DTO로 변환하는 생성자
    public NoticesDTO(Long id, String title, String content,
                      Integer priority,
                      LocalDateTime created_date,
                      String attachmentUrl) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.priority = priority;
        this.created_date = created_date;
        this.attachmentUrl = attachmentUrl;
    }
    // Notices 엔티티를 DTO로 변환하는 정적 메서드
    public NoticesDTO(Notices notice) {
        this.id = notice.getNoticeId();
        this.title = notice.getTitle();
        this.content = notice.getContent();
        this.priority = notice.getPriority();
        this.created_date = notice.getCreatedDate();
    }

}
