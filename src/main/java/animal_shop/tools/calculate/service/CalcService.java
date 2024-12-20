package animal_shop.tools.calculate.service;

import animal_shop.community.member.entity.Member;
import animal_shop.community.member.repository.MemberRepository;
import animal_shop.global.security.TokenProvider;
import animal_shop.shop.item.ItemSellStatus;
import animal_shop.shop.item.repository.ItemRepository;

import animal_shop.shop.main.dto.MainDTOBestResponse;
import animal_shop.shop.main.service.ShopService;
import animal_shop.shop.pet.entity.AnimalWeight;
import animal_shop.shop.pet.entity.Pet;
import animal_shop.shop.pet.repository.AnimalWeightRepository;
import animal_shop.tools.calculate.dto.AgeCalcDTO;
import animal_shop.tools.calculate.dto.CalorieCalcDTO;
import animal_shop.tools.calculate.dto.FoodCalcDTO;
import animal_shop.tools.calculate.dto.RecommendDTO;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;


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

    @Autowired
    private ShopService shopService;

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
            return shopService.searchItem(species, "food", null, ItemSellStatus.SELL);
        }
    }


    private MainDTOBestResponse recommendByAge(String species, RecommendDTO recommendDTO) {
        long age = recommendDTO.getHumanAge();

        if (species.equals("Dog")) {
            if (age <= 20) {
                return shopService.searchItem(species, "supplies", "toys", ItemSellStatus.SELL);
            } else if (age <= 60) {
                return shopService.searchItem(species, "supplies", "walking_supplies", ItemSellStatus.SELL);
            } else {
                return shopService.searchItem(species, "treats", "nutritional/functional", ItemSellStatus.SELL);
            }
        } else { // Cat
            if (age <= 20) {
                return shopService.searchItem(species, "supplies", "litter_boxes/bathroom_aids", ItemSellStatus.SELL);
            } else if (age <= 60) {
                return shopService.searchItem(species, "supplies", "fishing_rods/lasers", ItemSellStatus.SELL);
            } else {
                return shopService.searchItem(species, "treats", "nutritional/functional", ItemSellStatus.SELL);
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
            return shopService.searchItem("Dog", "treats", "nutritional/functional", ItemSellStatus.SELL);
        } else if (weight <= animalWeight.getHigh_weight()) {
            return shopService.searchItem("Dog", "treats", "bone_treats", ItemSellStatus.SELL);
        } else {
            return shopService.searchItem("Dog", "supplies", "walking_supplies", ItemSellStatus.SELL);
        }
    }

    private MainDTOBestResponse recommendCatByWeight(double weight, AnimalWeight animalWeight) {
        if (weight < animalWeight.getLow_weight()) {
            return shopService.searchItem("Cat", "treats", "nutritional/functional", ItemSellStatus.SELL);
        } else if (weight <= animalWeight.getHigh_weight()) {
            return shopService.searchItem("Cat", "supplies", "tunnels/hunting_instinct", ItemSellStatus.SELL);
        } else {
            return shopService.searchItem("Cat", "supplies", "cat_towers/cat_wheels", ItemSellStatus.SELL);
        }
    }

}
