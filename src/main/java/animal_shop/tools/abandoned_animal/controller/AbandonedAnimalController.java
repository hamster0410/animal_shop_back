package animal_shop.tools.abandoned_animal.controller;


import animal_shop.tools.abandoned_animal.service.AbandonedAnimalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/abandoned_animal")
public class AbandonedAnimalController {

    @Autowired
    AbandonedAnimalService abandonedAnimalService;

    @GetMapping("/find")
    public ResponseEntity<?> callForecastApi(){
        return abandonedAnimalService.storeAPIInfo();
    }
}