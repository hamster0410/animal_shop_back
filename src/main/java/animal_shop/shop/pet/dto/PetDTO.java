package animal_shop.shop.pet.dto;

import animal_shop.shop.pet.entity.Pet;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class PetDTO {

    private Long id; //동물 ID
    private String name; //동물 이름
    private Pet.PetSpecies species;  //동물 종(개 고양이 소동물)
    private String breed; //품종
    private boolean isNeutered;   //중성화 유무
    private int age;  //나이
    private Pet.Gender gender;  //성별
    private double weight; // 무게
    private String hasRegistrationCertificate;  //동물등록증 유무
    private String description; // 자기소개
    private String profileImageUrl;  //이미지URl

    public static PetDTO fromEntity(Pet pet) {
        return PetDTO.builder()
                .id(pet.getId())
                .name(pet.getName())
                .species(pet.getSpecies())
                .breed(pet.getBreed())
                .isNeutered(pet.getIsNeutered())
                .age(pet.getAge())
                .gender(pet.getGender())
                .weight(pet.getWeight())
                .hasRegistrationCertificate(pet.getHasRegistrationCertificate())
                .description(pet.getDescription())
                .profileImageUrl(pet.getProfileImageUrl())
                .build();

    }

    public boolean getIsNeutered() {
        return isNeutered;
    }
}
