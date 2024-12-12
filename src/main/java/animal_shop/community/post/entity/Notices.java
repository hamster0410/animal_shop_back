package animal_shop.community.post.entity;


import animal_shop.global.dto.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "notices")
@Getter
@Setter
public class Notices extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notice_id")
    private Long noticeId; // 공지사항 ID (자동 증가)

    @Column(nullable = false, length = 255)
    private String title; // 공지사항 제목

    @Column(nullable = false)
    private String content; // 공지사항 내용

    @Column(name = "sender_id", nullable = false)
    private Long senderId; // 발송자 ID (관리자 ID)

    @Column(name = "attachment_url", length = 255)
    private String attachmentUrl; // 첨부파일 URL

    @Column(nullable = false)
    private Integer priority = 1; // 우선순위 (기본값: 1)

}
