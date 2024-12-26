package animal_shop.shop.main.service;

import animal_shop.community.member.entity.Member;
import animal_shop.community.member.repository.MemberRepository;
import animal_shop.global.security.TokenProvider;
import animal_shop.shop.item.ItemSellStatus;
import animal_shop.shop.item.entity.Item;
import animal_shop.shop.item.repository.ItemRepository;
import animal_shop.shop.item.service.ItemSpecification;
import animal_shop.shop.main.dto.MainDTO;
import animal_shop.shop.main.dto.MainDTOBestResponse;
import animal_shop.shop.main.dto.MainDTOResponse;
import animal_shop.shop.pet.entity.AnimalWeight;
import animal_shop.shop.pet.entity.Pet;
import animal_shop.shop.pet.repository.AnimalWeightRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


@Slf4j
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

        List<MainDTO> animal_hot = itemRepository.findAllBySpeciesOrderByRatingPerComment(species,pageable).stream().map(MainDTO::new).toList();


        List<MainDTO> animal_custom = new ArrayList<>();
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
            log.info("pet is null");
            animal_custom = itemRepository.findBySpecies(species,ItemSellStatus.STOP,pageable).stream().map(MainDTO::new).toList();

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
            log.info("recommend through pet age");
            if(pet.getSpecies().equals(Pet.PetSpecies.DOG)){
                log.info("recommend age dog");
                if(pet.getAge()<2){
                    animal_custom = searchItem("Dog","food","puppy",ItemSellStatus.SELL).getGoods();

                }else if(pet.getAge() < 8){
                    animal_custom = searchItem("Dog","food","adult",ItemSellStatus.SELL).getGoods();

                }else{
                    animal_custom = searchItem("Dog","food","senior",ItemSellStatus.SELL).getGoods();
                }
            }else{
                log.info("recommend age cat");
                if(pet.getAge()<2){
                    animal_custom = searchItem("Cat","food","kitten",ItemSellStatus.SELL).getGoods();

                }else if(pet.getAge() < 8){
                    animal_custom = searchItem("Cat","food","adult",ItemSellStatus.SELL).getGoods();

                }else{
                    animal_custom = searchItem("Cat","treats","nutritional/functional",ItemSellStatus.SELL).getGoods();
                }
            }
        }else {
            log.info("recommend through pet weight");
            //체중기준    추천
            AnimalWeight animalWeight = animalWeightRepository.findByBreed(pet.getBreed());
            if(animalWeight == null){
                System.out.println("animal weight is null");
            };
            System.out.println("here " + pet.getSpecies());
            //강아지일 경우
            if(pet.getSpecies().equals(Pet.PetSpecies.DOG)){
                log.info("recommend weight dog");
                if (pet.getWeight() < animalWeight.getLow_weight()) {
                    animal_custom = searchItem("Dog","treats","nutritional/functional",ItemSellStatus.SELL).getGoods();
                } else if (pet.getWeight() > animalWeight.getHigh_weight()) {
                    animal_custom = searchItem("Dog","supplies","clothing/accessories",ItemSellStatus.SELL).getGoods();
                } else {
                    animal_custom = searchItem("Dog","supplies","walking_supplies",ItemSellStatus.SELL).getGoods();
                }
            }else{
                //고양이일 경우
                log.info("recommend weight cat");
                if (pet.getWeight() < animalWeight.getLow_weight()) {
                    animal_custom = searchItem("Cat","treats","nutritional/functional",ItemSellStatus.SELL).getGoods();
                } else if (pet.getWeight() > animalWeight.getHigh_weight()) {
                    animal_custom = searchItem("Cat","supplies","tunnels/hunting_instinct",ItemSellStatus.SELL).getGoods();
                } else {
                    animal_custom = searchItem("Cat","supplies","fishing_rods/lasers",ItemSellStatus.SELL).getGoods();
                }
            }
        }
        return MainDTOResponse.builder()
                .animal_new(animal_new)
                .animal_hot(animal_hot)
                .animal_custom(animal_custom)
                .build();
    }

    public MainDTOBestResponse new_contents(String species, int page) {
        Pageable pageable = PageRequest.of(page, 20, Sort.by(Sort.Direction.DESC, "createdDate"));

        Page<Item> best = itemRepository.findBySpecies(species,ItemSellStatus.STOP,pageable);
        return MainDTOBestResponse.builder()
                .goods(best.stream().map(MainDTO::new).toList())
                .total_count(best.getTotalElements())
                .build();
    }

    @Transactional(readOnly = true)
    public MainDTOBestResponse best_contents(String species, int page) {
        Pageable pageable = (Pageable) PageRequest.of(page,20);

        Page<Item> best = itemRepository.findAllBySpeciesOrderByRatingPerComment(species,pageable);
        return MainDTOBestResponse.builder()
                .goods(best.stream().map(MainDTO::new).toList())
                .total_count(best.getTotalElements())
                .build();
    }

    @Transactional(readOnly = true)
    public MainDTOBestResponse category_contents( String species, String category, String detailed_category, Boolean discount, int page) {

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

        if(discount != null){
            specification = specification.and(ItemSpecification.searchByDiscount(discount));
        }
        List<MainDTO> itemDTOs;
        long total_count;

        Pageable pageable = PageRequest.of(page, 16, Sort.by("createdDate").descending());
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

    @Transactional(readOnly = true)
    public MainDTOBestResponse searchItem( String species, String category, String detailed_category, ItemSellStatus itemSellStatus) {

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

        if (itemSellStatus != null) {
            if(itemSellStatus.equals(ItemSellStatus.STOP)){
                specification = specification.and(ItemSpecification.searchByItemStatusStop());
            }else{
                specification = specification.and(ItemSpecification.searchByItemStatusNotStop());
            }
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


