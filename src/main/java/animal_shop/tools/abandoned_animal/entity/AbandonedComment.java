package animal_shop.tools.abandoned_animal.entity;

import animal_shop.global.dto.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@Table(name = "abandoned_animals_comments")
public class AbandonedComment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)  // FetchType.LAZY로 설정 (적절한 전략 설정)
    @JoinColumn(name = "abandoned_animal_id")  // animal_id는 AbandonedAnimal의 id를 참조
    private AbandonedAnimal abandonedAnimal; // 댓글이 속한 동물 정보

    private String content; // 댓글 내용

    private Long userId; // 회원 ID를 저장

    private String author; // 사용자 이름 (username)을 저장


}


