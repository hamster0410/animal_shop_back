package animal_shop.shop.pet.controller;

import animal_shop.global.dto.ResponseDTO;
import animal_shop.shop.pet.dto.PetBreedList;
import animal_shop.shop.pet.dto.PetDTO;
import animal_shop.shop.pet.dto.PetProfileList;
import animal_shop.shop.pet.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pet")
public class PetController {
    @Autowired
    private PetService petService;

    //동물 등록
    @PostMapping("register")
    ResponseEntity<?> register_pet(@RequestHeader(value = "Authorization") String token,
                                   @RequestBody PetDTO petDTO) {
        ResponseDTO responseDTO = null;
        try {
            petService.registerPet(token, petDTO);
            responseDTO = ResponseDTO.builder()
                    .message("register success")
                    .build();
            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            responseDTO = ResponseDTO.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    //동물 삭제
    @DeleteMapping("delete/{petId}")
    ResponseEntity<?> delete_pet(@RequestHeader(value = "Authorization") String token,
                                 @PathVariable(value = "petId") String pet_id) {
        ResponseDTO responseDTO = null;
        try {
            petService.deletePet(token, pet_id);
            responseDTO = ResponseDTO.builder()
                    .message("delete success")
                    .build();
            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            responseDTO = ResponseDTO.builder()
                    .message(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    //동물 정보 수정
    @PatchMapping("/update/{petId}")
    ResponseEntity<?> update_pet(@RequestHeader(value = "Authorization") String token,
                                 @PathVariable(value = "petId") String petId,
                                 @RequestBody PetDTO petDTO) {
        ResponseDTO responseDTO = null;
        try {
            petService.updatePet(token, petId, petDTO);
            responseDTO = ResponseDTO.builder()
                    .message("update success")
                    .build();
            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            responseDTO = ResponseDTO.builder()
                    .message(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    //동물 전체 리스트 보기
    @GetMapping("/list")
    ResponseEntity<?> select_pet(@RequestHeader(value = "Authorization") String token,
                                 @RequestParam (value="page",defaultValue = "1")int page) {
        ResponseDTO responseDTO;
        try {
            PetProfileList petProfileList = petService.selectAll(token, page - 1);
            return ResponseEntity.ok().body(petProfileList);
        } catch (Exception e) {
            responseDTO = ResponseDTO.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }
    @PatchMapping("/leader/{petId}")
    ResponseEntity<?> updateLeader(@PathVariable Long petId) {
      ResponseDTO responseDTO = null;
        try {
            petService.updateLeader(petId);
            responseDTO = ResponseDTO.builder()
                    .message("leader success!")
                    .build();
            return ResponseEntity.ok().body(responseDTO);

        } catch (Exception e) {
            responseDTO = ResponseDTO.builder()
                    .message(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @GetMapping("/breed-list")
    ResponseEntity<?> getBreedList(@RequestParam(name = "species") String species) {

        ResponseDTO responseDTO = null;
        try {
            PetBreedList petBreedList = petService.getBreedList(species);
            return ResponseEntity.ok().body(petBreedList);

        } catch (Exception e) {
            responseDTO = ResponseDTO.builder()
                    .message(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }
}
