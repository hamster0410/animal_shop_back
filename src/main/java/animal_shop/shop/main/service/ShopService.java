package animal_shop.shop.main.service;


import animal_shop.shop.item.entity.Item;
import animal_shop.shop.item.repository.ItemRepository;
import animal_shop.shop.main.dto.MainDTO;
import animal_shop.shop.main.dto.MainDTOBestResponse;
import animal_shop.shop.main.dto.MainDTOResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@Service
public class ShopService {

    @Autowired
    ItemRepository itemRepository;

    public MainDTOResponse main_contents() {
        Pageable pageable = (Pageable) PageRequest.of(0,4);

        List<MainDTO> dog_hot = itemRepository.findBySpecies("dog",pageable).stream().map(MainDTO::new) // Item 객체를 MainDTO로 변환
                .toList();
        List<MainDTO> cat_hot = itemRepository.findBySpecies("cat",pageable).stream().map(MainDTO::new)
                .toList();
        List<MainDTO> new_goods = itemRepository.findAllByOrderByCreatedDateDesc(pageable).stream().map(MainDTO::new)
                .toList();

        return MainDTOResponse.builder()
                .cat_hot(cat_hot)
                .dog_hot(dog_hot)
                .new_goods(new_goods)
                .build();
    }

    public MainDTOBestResponse best_contents(int page) {
        Pageable pageable = (Pageable) PageRequest.of(page,20);

        Page<Item> best = itemRepository.findAll(pageable);
        return MainDTOBestResponse.builder()
                .best_goods(best.stream().map(MainDTO::new).toList())
                .total_count(best.getTotalElements())
                .build();

    }
    public MainDTOBestResponse category_contents(int page, String species, String category) {
        if (page < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Page index must be >= 0");
        }

        Pageable pageable = PageRequest.of(page, 20);
        Page<Item> category_goods = null;

        System.out.println("Species: " + species + ", Category: " + category);

        if (category != null) {
            category_goods = itemRepository.findBySpeciesAndCategoryWithThumbnails(species, category, pageable);
        } else if (species != null) {
            category_goods = itemRepository.findBySpecies(species, pageable);
        } else {
            category_goods = itemRepository.findAll(pageable);
        }

        return MainDTOBestResponse.builder()
                .best_goods(category_goods.stream().map(MainDTO::new).toList())
                .total_count(category_goods.getTotalElements())
                .build();
    }

}


