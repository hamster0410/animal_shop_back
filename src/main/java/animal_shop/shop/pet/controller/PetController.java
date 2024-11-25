package animal_shop.shop.pet.controller;

import animal_shop.global.dto.ResponseDTO;
import animal_shop.shop.pet.service.PetService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestHeader;

public class PetController {
    @PatchMapping("register/new")
    ResponseEntity<?> register_pet(@RequestHeader(value = "Authorization")String token) {
        ResponseDTO responseDTO = null;
        try{
            PetService.register_API(token);
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
}
