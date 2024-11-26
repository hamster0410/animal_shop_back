package animal_shop.shop.pet.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PetDTOList {

    public Long id; // 아이디

    public String name; //이름

    public String profileImageUrl; // 대표이미지

    public int age;

    public double weight;

    public String breed;

}
