package animal_shop.shop.main.controller;

import animal_shop.global.dto.ResponseDTO;
import animal_shop.shop.main.dto.MainDTOBestResponse;
import animal_shop.shop.main.dto.MainDTOResponse;
import animal_shop.shop.main.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/best")
    public ResponseEntity<?> shop_best(@RequestParam(value = "page", defaultValue = "1")int page){
        ResponseDTO responseDTO;
        try{
            MainDTOBestResponse mainDTOBestResponse = shopService.best_contents(page -1);

            return ResponseEntity.ok().body(mainDTOBestResponse);

        }catch (Exception e){

            responseDTO = ResponseDTO.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }
    @GetMapping("/product-category/{species}/{category}")
    public ResponseEntity<?> shop_category(@RequestParam(value = "page", defaultValue = "1")int page,
                                           @PathVariable(value = "species") String species,
                                           @PathVariable(value = "category") String category){
        ResponseDTO responseDTO;
        try{
            MainDTOBestResponse mainDTOBestResponse = shopService.category_contents(page -1,species,category);

            return ResponseEntity.ok().body(mainDTOBestResponse);

        }catch (Exception e){

            responseDTO = ResponseDTO.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }
}
