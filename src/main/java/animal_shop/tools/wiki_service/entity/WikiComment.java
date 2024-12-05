package animal_shop.tools.wiki_service.entity;


import animal_shop.community.member.entity.Member;
import animal_shop.global.dto.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.stereotype.Service;

@Entity
@Table(name = "wiki_comment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class WikiComment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content", nullable = false, length = 1000)
    private String content; // 댓글 내용

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wiki_id", nullable = false)
    private Wiki wiki; // 연결된 Wiki

    @Column(name = "author", nullable = false)
    private String author; // 작성자


}