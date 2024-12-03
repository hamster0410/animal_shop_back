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
    public ResponseEntity<?> shop_main(
            @RequestHeader(value = "Authorization", required = false) String token,
            @RequestParam(value = "species", defaultValue = "dog")String species){
        ResponseDTO responseDTO;
        try{
            MainDTOResponse mainDTOResponse = shopService.main_contents(token, species);

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

    @GetMapping("/product-category")
    public ResponseEntity<?> product_categoy(
            @RequestParam(value = "species", required = false, defaultValue = "dog")String species,
            @RequestParam(value = "category", required = false)String category,
            @RequestParam(value = "detailed_category", required = false)String detailed_category,
            @RequestParam(value = "page",required = false, defaultValue = "1")int page
            ){
        ResponseDTO responseDTO;
        try{
            MainDTOBestResponse mainDTOBestResponse = shopService.category_contents(species, category,detailed_category, page-1);

            return ResponseEntity.ok().body(mainDTOBestResponse);

        }catch (Exception e){

            responseDTO = ResponseDTO.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }
}
