package animal_shop.tools.calculate.dto;

import animal_shop.shop.pet.entity.Pet;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AgeCalcDTO {

    private Pet.PetSpecies species; // 종 (개, 고양이, 소동물)

    private int age;

    private String size;

}
