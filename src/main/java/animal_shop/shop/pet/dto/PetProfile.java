package animal_shop.shop.pet.dto;


import animal_shop.shop.pet.entity.Pet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PetProfile {

    public Long id; // 아이디

    public String name; //이름

    public String profileImageUrl; // 대표이미지

    public int age;

    public double weight;

    public String breed;

    private Boolean isNeutered; // 중성화 유무 (중성화 여부)

    private Pet.Gender gender; // 성별

    private Boolean isLeader;

    private String species;

    private String description;

    public PetProfile(Pet pet) {
        this.id = pet.getId();
        this.name = pet.getName();
        this.profileImageUrl = pet.getProfileImageUrl();
        this.age = pet.getAge();
        this.weight = pet.getWeight();
        this.breed = pet.getBreed();
        this.isNeutered = pet.getIsNeutered();
        this.gender = pet.getGender();
        this.isLeader = pet.getLeader();
        if(pet.getSpecies().equals(Pet.PetSpecies.DOG)){
            this.species = "DOG";
        }else if(pet.getSpecies().equals(Pet.PetSpecies.CAT)){
            this.species = "CAT";
        }
        this.description = pet.getDescription();
    }
}
