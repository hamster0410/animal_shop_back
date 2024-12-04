package animal_shop.tools.abandoned_animal.controller;


import animal_shop.global.dto.ResponseDTO;
import animal_shop.tools.abandoned_animal.dto.AnimalDTO;
import animal_shop.tools.abandoned_animal.dto.AnimalDetailDTO;
import animal_shop.tools.abandoned_animal.dto.AnimalListDTOResponse;
import animal_shop.tools.abandoned_animal.dto.AnimalSearchDTO;
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

    @PostMapping("/search")
    public ResponseEntity<?> findAnimal(@RequestBody AnimalSearchDTO animalSearchDTO,
                                        @RequestParam(value = "page", defaultValue = "1", required = false)int page){
        try{
            AnimalListDTOResponse animalListDTOResponse =  abandonedAnimalService.searchAPIInfo(animalSearchDTO, page-1);
            return ResponseEntity.ok().body(animalListDTOResponse);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/detail")
    public ResponseEntity<?> detailAnimal(@RequestParam(value = "animalId") Long animalId){
        ResponseDTO responseDTO;
        try{
            AnimalDetailDTO animalDetailDTO =  abandonedAnimalService.searchDetailAPI(animalId);
            return ResponseEntity.ok().body(animalDetailDTO);
        }catch (Exception e){
            responseDTO = ResponseDTO.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }
}