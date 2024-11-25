package animal_shop.shop.pet.controller;

import animal_shop.global.dto.ResponseDTO;
import animal_shop.shop.pet.dto.PetDTO;
import animal_shop.shop.pet.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pet")
public class PetController {
    @Autowired
    private PetService petService;

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
    @DeleteMapping("delete/{pet_id}")
    ResponseEntity<?> delete_pet(@RequestHeader(value="Authorization")String token,
                                 @PathVariable(value="pet_id")String pet_id){
        ResponseDTO responseDTO = null;
        try{
            petService.deletePet(token,pet_id);
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
}
