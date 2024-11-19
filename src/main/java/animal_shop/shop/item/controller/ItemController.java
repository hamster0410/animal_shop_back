package animal_shop.shop.item.controller;

import animal_shop.global.dto.ResponseDTO;
import animal_shop.shop.item.dto.ItemDetailDTO;
import animal_shop.shop.item.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/item")
public class ItemController {

    @Autowired
    ItemService itemService;

    @GetMapping("/detail/{itemId}")
    public ResponseEntity<?> item_detail(@PathVariable(value = "itemId")String itemId){
        ResponseDTO responseDTO = null;
        try{
            ItemDetailDTO itemDetailDTO = itemService.findById(itemId);

            return ResponseEntity.ok().body(itemDetailDTO);
        }catch (Exception e){
            responseDTO = ResponseDTO.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }
}
