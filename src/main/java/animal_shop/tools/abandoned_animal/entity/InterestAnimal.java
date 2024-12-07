package animal_shop.tools.abandoned_animal.entity;

import animal_shop.tools.abandoned_animal.dto.InterestAnimalDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "interest_animal")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InterestAnimal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;    // 동물번호

    private String filename;  // 썸네일 이미지 파일명
    private String name;   // 동물 이름
    private long age;   // 나이
    private String noticeSdt; // 공고 시작일
    private String noticeEdt; // 공고 종료일
    private String careNm;  // 보호소 이름
    private String careTel;  // 보호소 전화번호

    // DTO에서 엔티티로 변환하는 메서드
    public static InterestAnimal fromDTO(InterestAnimalDTO dto) {
        return InterestAnimal.builder()
                .id(dto.getId())
                .filename(dto.getFilename())
                .name(dto.getName())
                .age(dto.getAge())
                .noticeSdt(dto.getNoticeSdt())
                .noticeEdt(dto.getNoticeEdt())
                .careNm(dto.getCareNm())
                .careTel(dto.getCareTel())
                .build();
    }
}
