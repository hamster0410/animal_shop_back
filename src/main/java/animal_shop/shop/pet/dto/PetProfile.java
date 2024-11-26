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

    public PetProfile(Pet pet) {
        this.id = pet.getId();
        this.name = pet.getName();
        this.profileImageUrl = pet.getProfileImageUrl();
        this.age = pet.getAge();
        this.weight = pet.getWeight();
        this.breed = pet.getBreed();
    }
}