package animal_shop.shop.main.controller;

import animal_shop.global.dto.ResponseDTO;
import animal_shop.shop.main.dto.MainDTOResponse;
import animal_shop.shop.main.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/shop")
public class ShopMainController {

    @Autowired
    ShopService shopService;

    @GetMapping("/main")
    public ResponseEntity<?> shop_main(){
        ResponseDTO responseDTO;
        try{
            MainDTOResponse mainDTOResponse = shopService.main_contents();

            return ResponseEntity.ok().body(mainDTOResponse);

        }catch (Exception e){

            responseDTO = ResponseDTO.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }
}
