package animal_shop.tools.abandoned_animal.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class AnimalSearchDTO {
    private String sex; //M, F, Q
    private String neuter; //Y, N, U
    private List<Integer> age; //1,5,9,10
    private List<String> location; //서울특별시 은평구
    private List<String> breed; // 믹스견, 시골잡종
    private String species; // 개, 고양이
}
