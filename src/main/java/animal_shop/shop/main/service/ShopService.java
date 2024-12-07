package animal_shop.shop.main.service;

import animal_shop.community.member.entity.Member;
import animal_shop.community.member.repository.MemberRepository;
import animal_shop.global.security.TokenProvider;
import animal_shop.shop.item.ItemSellStatus;
import animal_shop.shop.item.entity.Item;
import animal_shop.shop.item.repository.ItemRepository;
import animal_shop.shop.main.dto.MainDTO;
import animal_shop.shop.main.dto.MainDTOBestResponse;
import animal_shop.shop.main.dto.MainDTOResponse;
import animal_shop.shop.pet.entity.AnimalWeight;
import animal_shop.shop.pet.entity.Pet;
import animal_shop.shop.pet.repository.AnimalWeightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


@Service
public class ShopService {

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    TokenProvider tokenProvider;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    AnimalWeightRepository animalWeightRepository;

    @Transactional(readOnly = true)
    public MainDTOResponse main_contents(String token, String species) {
        Pageable pageable = (Pageable) PageRequest.of(0,4);

        List<MainDTO> animal_new = itemRepository.findBySpecies(species,ItemSellStatus.STOP,pageable).stream().map(MainDTO::new) // Item 객체를 MainDTO로 변환
                .toList();

        List<MainDTO> animal_hot = itemRepository.findBySpecies(species,ItemSellStatus.STOP,pageable).stream().map(MainDTO::new) // Item 객체를 MainDTO로 변환
                .toList();

        List<MainDTO> animal_custom = new ArrayList<>();
        System.out.println(token  + " " +  species);
        //로그인 하지 않은 경우
        if(token==null){
            animal_custom = itemRepository.findBySpecies(species,ItemSellStatus.STOP,pageable).stream().map(MainDTO::new).toList();
            System.out.println("login is null");

            return MainDTOResponse.builder()
                    .animal_new(animal_new)
                    .animal_hot(animal_hot)
                    .animal_custom(animal_custom  )
                    .build();
        }

        String userId = tokenProvider.extractIdByAccessToken(token);
        Member member = memberRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new IllegalArgumentException("member is not found"));
        Pet pet = member.getPets().stream()
                .filter(Pet::getLeader) // leader 값이 true인 Pet만 필터링
                .findFirst() // 첫 번째 값을 가져옴
                .orElse(null); // 없으면 null 반환.

        //애완동물 정보가 없거나 리더 동물이 없는경우
        if(pet == null){
            animal_custom = itemRepository.findBySpecies(species,ItemSellStatus.STOP,pageable).stream().map(MainDTO::new).toList();
            System.out.println("pet is null");
            return MainDTOResponse.builder()
                    .animal_new(animal_new)
                    .animal_hot(animal_hot)
                    .animal_custom(animal_custom  )
                    .build();
        }
        Random random = new Random();
        // 1부터 10까지 랜덤 숫자 출력
        int randomNumber = random.nextInt(10) + 1;
        //나이기준 추천
        if(randomNumber % 2 ==0){
            if(species.equals("dog")){
                if(pet.getAge()<2){
                    animal_custom = itemRepository.findBySpeciesCategoryAndDetailedCategoryWithThumbnails(species,"food","puppy", ItemSellStatus.STOP,
                                    pageable)
                            .stream().map(MainDTO::new).toList();
                }else if(pet.getAge() < 8){
                    animal_custom = itemRepository.findBySpeciesCategoryAndDetailedCategoryWithThumbnails(species,"food","adult", ItemSellStatus.STOP,
                                    pageable)
                            .stream().map(MainDTO::new).toList();
                }else{
                    animal_custom = itemRepository.findBySpeciesCategoryAndDetailedCategoryWithThumbnails(species,"food","senior", ItemSellStatus.STOP,
                                    pageable)
                            .stream().map(MainDTO::new).toList();
                }
            }else{
                if(pet.getAge()<2){
                    animal_custom = itemRepository.findBySpeciesCategoryAndDetailedCategoryWithThumbnails(species,"food","kitten", ItemSellStatus.STOP,
                                    pageable)
                            .stream().map(MainDTO::new).toList();
                }else if(pet.getAge() < 8){
                    animal_custom = itemRepository.findBySpeciesCategoryAndDetailedCategoryWithThumbnails(species,"food","adult", ItemSellStatus.STOP,
                                    pageable)
                            .stream().map(MainDTO::new).toList();
                }else{
                    animal_custom = itemRepository.findBySpeciesCategoryAndDetailedCategoryWithThumbnails(species,"food","nutritional/functional", ItemSellStatus.STOP,
                                    pageable)
                            .stream().map(MainDTO::new).toList();
                }
            }
        }else {
            //체중기준 추천
            AnimalWeight animalWeight = animalWeightRepository.findBySpecies(pet.getBreed());
            //강아지일 경우
            if(species.equals("dog")){
                if (pet.getWeight() < animalWeight.getLow_weight()) {
                    animal_custom = itemRepository.findBySpeciesCategoryAndDetailedCategoryWithThumbnails(species,"food","nutritional/functional", ItemSellStatus.STOP,
                                    pageable)
                            .stream().map(MainDTO::new).toList();
                } else if (pet.getWeight() > animalWeight.getHigh_weight()) {
                    animal_custom = itemRepository.findBySpeciesCategoryAndDetailedCategoryWithThumbnails(species,"supplies","clothing/accessories", ItemSellStatus.STOP,
                                    pageable)
                            .stream().map(MainDTO::new).toList();
                } else {
                    animal_custom = itemRepository.findBySpeciesCategoryAndDetailedCategoryWithThumbnails(species,"supplies","walking_supplies", ItemSellStatus.STOP,
                                    pageable)
                            .stream().map(MainDTO::new).toList();
                }
            }else{
                //고양이일 경우
                if (pet.getWeight() < animalWeight.getLow_weight()) {
                    animal_custom = itemRepository.findBySpeciesCategoryAndDetailedCategoryWithThumbnails(species,"food","nutritional/functional", ItemSellStatus.STOP,pageable)
                            .stream().map(MainDTO::new).toList();
                } else if (pet.getWeight() > animalWeight.getHigh_weight()) {
                    animal_custom = itemRepository.findBySpeciesCategoryAndDetailedCategoryWithThumbnails(species,"supplies","tunnels/hunting_instinct", ItemSellStatus.STOP,pageable)
                            .stream().map(MainDTO::new).toList();
                } else {
                    animal_custom = itemRepository.findBySpeciesCategoryAndDetailedCategoryWithThumbnails(species,"supplies","fishing_rods/lasers", ItemSellStatus.STOP,pageable)
                            .stream().map(MainDTO::new).toList();
                }
            }
        }
        return MainDTOResponse.builder()
                .animal_new(animal_new)
                .animal_hot(animal_hot)
                .animal_custom(animal_custom  )
                .build();
    }

    @Transactional(readOnly = true)
    public MainDTOBestResponse best_contents(int page) {
        Pageable pageable = (Pageable) PageRequest.of(page,20);

        Page<Item> best = itemRepository.findAll(pageable);
        return MainDTOBestResponse.builder()
                .goods(best.stream().map(MainDTO::new).toList())
                .total_count(best.getTotalElements())
                .build();

    }
    @Transactional(readOnly = true)
    public MainDTOBestResponse category_contents(String species, String category, String detailed_category, int page) {
        if (page < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Page index must be >= 0");
        }

        Pageable pageable = PageRequest.of(page, 20);
        Page<Item> category_goods = null;


        if (detailed_category != null && category != null) {
            category_goods = itemRepository.findBySpeciesCategoryAndDetailedCategoryWithThumbnails(species,category,detailed_category,ItemSellStatus.STOP,pageable);
        } else if (category != null) {
            category_goods = itemRepository.findBySpeciesAndCategoryWithThumbnails(species,category,ItemSellStatus.STOP,pageable);
        } else {
            category_goods = itemRepository.findBySpecies(species,ItemSellStatus.STOP, pageable);
        }

        return MainDTOBestResponse.builder()
                .goods(category_goods.stream().map(MainDTO::new).toList())
                .total_count(category_goods.getTotalElements())
                .build();
    }
}


