package animal_shop.tools.abandoned_animal.dto;

import lombok.Data;

@Data
public class ByeAnimalDTO {
    private String desertionNo; // 동물 등록증 번호
    private String newState; // 상태변화
    private String careNm; //보호소 이름
    private String kindCd; //품종 코드 (예: [개 / 고양이] + 품종명)


}