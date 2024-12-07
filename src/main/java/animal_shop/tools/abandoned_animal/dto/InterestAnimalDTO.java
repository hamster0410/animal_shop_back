package animal_shop.tools.abandoned_animal.dto;

import animal_shop.community.member.entity.Member;
import animal_shop.tools.abandoned_animal.entity.InterestAnimal;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InterestAnimalDTO {

    private String attachmentUrl;   // 첨부파일 URL

    private Long id;    //동물번호

    private String name;   //동물 이름

    private String careNm;  //보호소 이름

    public static InterestAnimal toEntity(InterestAnimalDTO dto, Member member) {
        return InterestAnimal.builder()
//                .id(dto.getId())
                .name(dto.getName())
                .careNm(dto.getCareNm())
                .attachmentUrl(dto.getAttachmentUrl())
                .member(member) // 해당 관심동물에 연결된 Member 설정
                .build();
    }
}
