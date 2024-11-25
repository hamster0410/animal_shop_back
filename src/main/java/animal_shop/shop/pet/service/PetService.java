package animal_shop.shop.pet.service;

import animal_shop.shop.pet.controller.PetController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PetService {

    @Transactional
    public void registerAPI(String token) {
        // 로직 구현
    }
}
