package animal_shop.shop.pet.dto;

import animal_shop.shop.pet.entity.PetEntity;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PetDTO {

    private Long id; //동물 ID
    private String name; //동물 이름
    private PetEntity.PetSpecies species;  //동물 종(개 고양이 소동물)
    private String breed; //품종
    private boolean isNeutered;   //중성화 유무
    private int age;  //나이
    private PetEntity.Gender gender;  //성별
    private double weight; // 무게
    private String hasRegistrationCertificate;  //동물등록증 유무
    private String description; // 자기소개
    private String profileImageUrl;  //이미지URl

    public static PetDTO PetEntity(PetEntity petEntity){
        return  PetDTO.builder()
                .id(petEntity.getId())
                .name(petEntity.getName())
                .species(petEntity.getSpecies())
                .breed(petEntity.getBreed())
                .isNeutered(petEntity.getIsNeutered() != null ? petEntity.getIsNeutered() : false)
                .age(petEntity.getAge())
                .gender(petEntity.getGender())
                .weight(petEntity.getWeight())
                .hasRegistrationCertificate(petEntity.getHasRegistrationCertificate())
                .description(petEntity.getDescription())
                .profileImageUrl(petEntity.getProfileImageUrl())
                .build();

    }
}
