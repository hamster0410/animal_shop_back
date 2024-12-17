package animal_shop.tools.calculate.service;

import animal_shop.community.member.entity.Member;
import animal_shop.community.member.repository.MemberRepository;
import animal_shop.global.security.TokenProvider;
import animal_shop.shop.item.entity.Item;
import animal_shop.shop.item.repository.ItemRepository;
import animal_shop.shop.item.service.ItemSpecification;
import animal_shop.shop.main.dto.MainDTO;
import animal_shop.shop.main.dto.MainDTOBestResponse;
import animal_shop.shop.pet.entity.AnimalWeight;
import animal_shop.shop.pet.entity.Pet;
import animal_shop.shop.pet.repository.AnimalWeightRepository;
import animal_shop.tools.calculate.dto.AgeCalcDTO;
import animal_shop.tools.calculate.dto.CalorieCalcDTO;
import animal_shop.tools.calculate.dto.FoodCalcDTO;
import animal_shop.tools.calculate.dto.RecommendDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class CalcService {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private AnimalWeightRepository animalWeightRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private TokenProvider tokenProvider;

    public AgeCalcDTO ageCalc(String token) {
        String userId = tokenProvider.extractIdByAccessToken(token);
        Member member = memberRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new IllegalArgumentException("member is not found"));


        Pet leader = member.getPets().stream()
                .filter(Pet::getLeader) // leader 값이 true인 Pet만 필터링
                .findFirst() // 첫 번째 값을 가져옴
                .orElse(null); // 없으면 null 반환.

        AnimalWeight animalWeight = animalWeightRepository.findByBreed(leader.getBreed());
        if(animalWeight==null){
            throw new IllegalArgumentException("animal info error");
        }


        return AgeCalcDTO.builder()
                .age(leader.getAge())
                .size(animalWeight.getSize())
                .species(leader.getSpecies())
                .build();

    }

    public CalorieCalcDTO calorieCalc(String token) {
        String userId = tokenProvider.extractIdByAccessToken(token);
        Member member = memberRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new IllegalArgumentException("member is not found"));


        Pet leader = member.getPets().stream()
                .filter(Pet::getLeader) // leader 값이 true인 Pet만 필터링
                .findFirst() // 첫 번째 값을 가져옴
                .orElse(null); // 없으면 null 반환.


        AnimalWeight animalWeight = animalWeightRepository.findByBreed(leader.getBreed());

        if(animalWeight==null){
            throw new IllegalArgumentException("animal info error");
        }

        return CalorieCalcDTO.builder()
                .age(leader.getAge())
                .size(animalWeight.getSize())
                .species(leader.getSpecies())
                .breed(leader.getBreed())
                .isNeutered(leader.getIsNeutered())
                .weight(leader.getWeight())
                .build();
    }

    public FoodCalcDTO foodCalc(String token) {

        String userId = tokenProvider.extractIdByAccessToken(token);
        Member member = memberRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new IllegalArgumentException("member is not found"));


        Pet leader = member.getPets().stream()
                .filter(Pet::getLeader) // leader 값이 true인 Pet만 필터링
                .findFirst() // 첫 번째 값을 가져옴
                .orElse(null); // 없으면 null 반환.


        AnimalWeight animalWeight = animalWeightRepository.findByBreed(leader.getBreed());

        if(animalWeight==null){
            throw new IllegalArgumentException("animal info error");
        }
        boolean is_big = false;
        if(animalWeight.getSize().equals("large")) is_big = true;
        return FoodCalcDTO.builder()
                .age(leader.getAge())
                .species(leader.getSpecies())
                .isBig(is_big)
                .build();

    }

    public MainDTOBestResponse recommend(String searchBy, RecommendDTO recommendDTO) {
        String species = recommendDTO.getSpecies().equalsIgnoreCase("DOG") ? "Dog" : "Cat";

        if (searchBy.equalsIgnoreCase("age")) {
            return recommendByAge(species, recommendDTO);
        } else if (searchBy.equalsIgnoreCase("calorie")) {
            return recommendByCalorie(species, recommendDTO);
        } else {
            return searchItem(species, "food", null);
        }
    }


    private MainDTOBestResponse recommendByAge(String species, RecommendDTO recommendDTO) {
        long age = recommendDTO.getHumanAge();

        if (species.equals("Dog")) {
            if (age <= 20) {
                return searchItem(species, "supplies", "toys");
            } else if (age <= 60) {
                return searchItem(species, "supplies", "walking_supplies");
            } else {
                return searchItem(species, "treats", "nutritional/functional");
            }
        } else { // Cat
            if (age <= 20) {
                return searchItem(species, "supplies", "litter_boxes/bathroom_aids");
            } else if (age <= 60) {
                return searchItem(species, "supplies", "fishing_rods/lasers");
            } else {
                return searchItem(species, "treats", "nutritional/functional");
            }
        }
    }

    private MainDTOBestResponse recommendByCalorie(String species, RecommendDTO recommendDTO) {
        String breed = recommendDTO.getBreed();
        AnimalWeight animalWeight = animalWeightRepository.findByBreed(breed);

        if (animalWeight == null) {
            throw new IllegalArgumentException("Breed not found");
        }

        double weight = recommendDTO.getWeight();

        if (species.equals("Dog")) {
            return recommendDogByWeight(weight, animalWeight);
        } else { // Cat
            return recommendCatByWeight(weight, animalWeight);
        }
    }

    private MainDTOBestResponse recommendDogByWeight(double weight, AnimalWeight animalWeight) {
        if (weight < animalWeight.getLow_weight()) {
            return searchItem("Dog", "treats", "nutritional/functional");
        } else if (weight <= animalWeight.getHigh_weight()) {
            return searchItem("Dog", "treats", "bone_treats");
        } else {
            return searchItem("Dog", "supplies", "walking_supplies");
        }
    }

    private MainDTOBestResponse recommendCatByWeight(double weight, AnimalWeight animalWeight) {
        if (weight < animalWeight.getLow_weight()) {
            return searchItem("Cat", "treats", "nutritional/functional");
        } else if (weight <= animalWeight.getHigh_weight()) {
            return searchItem("Cat", "supplies", "tunnels/hunting_instinct");
        } else {
            return searchItem("Cat", "supplies", "cat_towers/cat_wheels");
        }
    }

    @Transactional(readOnly = true)
    public MainDTOBestResponse searchItem( String species, String category, String detailed_category) {

        Specification<Item> specification = Specification.where(null);

        if (species != null) {
            specification = specification.and(ItemSpecification.searchBySpecies(species));
        }

        if (category != null) {
            specification = specification.and(ItemSpecification.searchByCategory(category));
        }

        if (detailed_category != null) {
            specification = specification.and(ItemSpecification.searchByDetailedCategory(detailed_category));
        }

        specification = specification.and(ItemSpecification.searchByItemStatusNotStop());

        List<MainDTO> itemDTOs;
        long total_count;

        Pageable pageable = PageRequest.of(0, 4, Sort.by("createdDate").descending());
        Page<Item> items = itemRepository.findAll(specification, pageable);
        total_count = items.getTotalElements();
        itemDTOs = items.map(MainDTO::new).getContent();

        // DTO 변환 (Item -> ItemDetailDTO)
        // 검색 결과 반환
        return MainDTOBestResponse.builder()
                .goods(itemDTOs)
                .total_count(total_count)
                .build();
    }

}
