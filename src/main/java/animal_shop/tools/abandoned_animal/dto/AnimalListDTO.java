package animal_shop.tools.abandoned_animal.dto;

import animal_shop.tools.abandoned_animal.entity.AbandonedAnimal;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class AnimalListDTO {
    private Long id;
    private Long age;  // 나이 (예: 2019년생)
    private String sex;
    private String location;
    private String org_nm;
    private String species;
    private String status;
    private String popfile;
    public String neuterYn;
    public String desertion_no;

    public AnimalListDTO(AbandonedAnimal animal){
        this.id = animal.getId();
        this.age = LocalDate.now().getYear() - animal.getAge();
        this.sex = animal.getSexCd();
        String [] a = animal.getCareAddr().split(" ");
        this.location = a[0] + a[1];
        this.species = animal.getKindCd();
        this.status = animal.getProcessState();
        this.popfile = animal.getPopfile();
        this.neuterYn = animal.getNeuterYn();
        this.org_nm = animal.getOrgNm();
        this.desertion_no = animal.getDesertionNo();
    }
}
