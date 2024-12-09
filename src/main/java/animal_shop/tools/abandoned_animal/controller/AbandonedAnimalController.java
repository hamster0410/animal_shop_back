package animal_shop.tools.abandoned_animal.controller;


import animal_shop.global.dto.ResponseDTO;
import animal_shop.tools.abandoned_animal.dto.*;
import animal_shop.tools.abandoned_animal.service.AbandonedAnimalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/abandoned_animal")
public class AbandonedAnimalController {

    @Autowired
    AbandonedAnimalService abandonedAnimalService;

    @GetMapping("/find")
    public ResponseEntity<?> callForecastApi(){
        return abandonedAnimalService.storeAPIInfo();
    }

    @GetMapping("/interest-list")
    public ResponseEntity<?> listInterestAnimal(@RequestBody AnimalSearchDTO animalSearchDTO,
                                                @RequestParam(value = "page", defaultValue = "1", required = false)int page){
        try{
            AnimalListDTOResponse animalListDTOResponse =  abandonedAnimalService.searchAPIInfo(animalSearchDTO, page-1);
            return ResponseEntity.ok().body(animalListDTOResponse);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
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
    // 관심동물 등록
    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> registerInterest(@RequestHeader(value = "Authorization") String token,
                                              @RequestPart(value = "attachmentUrl") MultipartFile file,
                                              @RequestPart(value = "interestAnimalDTO") InterestAnimalDTO interestAnimalDTO,
                                              @RequestParam(value = "page", defaultValue = "1", required = false) int page) {
        ResponseDTO responseDTO = null;
        try {
            // 관심동물 등록 처리
            abandonedAnimalService.interestAnimal(token, interestAnimalDTO, page - 1, file);
            // 성공 메시지 반환
            responseDTO = ResponseDTO.builder()
                    .message("success register")
                    .build();
            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            // 오류 발생 시 메시지 반환
            responseDTO = ResponseDTO.builder()
                    .message(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?>deleteInterest(@RequestHeader(value = "Authorization")String token,
                                           @PathVariable Long id){
        ResponseDTO responseDTO = null;
        try{
            abandonedAnimalService.indifferentAnimal(token,id);
            responseDTO = ResponseDTO.builder()
                    .message("success delete")
                    .build();
            return ResponseEntity.ok().body(responseDTO);
        }catch (Exception e){
            responseDTO = ResponseDTO.builder()
                    .message(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }
}