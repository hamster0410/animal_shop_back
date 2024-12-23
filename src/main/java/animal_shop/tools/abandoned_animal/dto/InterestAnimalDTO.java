package animal_shop.tools.abandoned_animal.dto;

import animal_shop.tools.abandoned_animal.entity.InterestAnimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InterestAnimalDTO {
    private long id;

    private String attachmentUrl;   // 첨부파일 URL

    private String kind_cd;

    private String neuter_yn;

    private String special_mark;

    private String sex_cd;

    private long age;

    private String care_nm;

    private long abandoned_animal_id;

    public InterestAnimalDTO(InterestAnimal interestAnimal) {
        this.id = interestAnimal.getId();
        this.abandoned_animal_id = interestAnimal.getAbandonedAnimalId();
        this.attachmentUrl = interestAnimal.getAttachment_url();
        this.kind_cd = interestAnimal.getKind_cd();
        this.neuter_yn = interestAnimal.getNeuter_yn();
        this.special_mark = interestAnimal.getSpecial_mark();
        this.sex_cd = interestAnimal.getSex_cd();
        this.age = interestAnimal.getAge();
        this.care_nm = interestAnimal.getCare_nm();
    }
}
