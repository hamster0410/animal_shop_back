package animal_shop.tools.abandoned_animal.entity;

import animal_shop.community.member.entity.Member;
import animal_shop.tools.abandoned_animal.dto.InterestAnimalDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "interest_animal")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class InterestAnimal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "attachmentUrl") // DB 컬럼명과 동일
    private String attachmentUrl;   // 첨부파일 URL

    @Column(name = "name") // DB 컬럼명과 동일
    private String name;

    @Column(name = "care_nm") // DB 컬럼명이 필드명과 다를 경우
    private String careNm;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id") // FK 설정
    private Member member;


}
