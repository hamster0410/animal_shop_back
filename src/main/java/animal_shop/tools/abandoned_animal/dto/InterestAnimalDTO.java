package animal_shop.tools.abandoned_animal.dto;

import lombok.Data;

@Data
public class InterestAnimalDTO {

    private String filename;  //썸네일 이미지 파일명

    private Long id;    //동물번호

    private String name;   //동물 이름

    private long age;   //나이

    private String noticeSdt; //공고 시작일

    private String noticeEdt; //공고 종료일

    private String careNm;  //보호소 이름

    private String careTel;  //보호소 전화번호


}
