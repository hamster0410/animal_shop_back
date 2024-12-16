package animal_shop.tools.calculate.service;

import animal_shop.community.member.entity.Member;
import animal_shop.community.member.repository.MemberRepository;
import animal_shop.global.security.TokenProvider;
import animal_shop.shop.pet.entity.AnimalWeight;
import animal_shop.shop.pet.entity.Pet;
import animal_shop.shop.pet.repository.AnimalWeightRepository;
import animal_shop.tools.calculate.dto.AgeCalcDTO;
import animal_shop.tools.calculate.dto.CalorieCalcDTO;
import animal_shop.tools.calculate.dto.FoodCalcDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CalcService {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private AnimalWeightRepository animalWeightRepository;

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

        AnimalWeight animalWeight = animalWeightRepository.findBySpecies(leader.getBreed());
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


        AnimalWeight animalWeight = animalWeightRepository.findBySpecies(leader.getBreed());

        if(animalWeight==null){
            throw new IllegalArgumentException("animal info error");
        }

        return CalorieCalcDTO.builder()
                .age(leader.getAge())
                .size(animalWeight.getSize())
                .species(leader.getSpecies())
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


        AnimalWeight animalWeight = animalWeightRepository.findBySpecies(leader.getBreed());

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
}
