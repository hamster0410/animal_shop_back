package animal_shop.shop.item.controller;

import animal_shop.global.dto.ResponseDTO;
import animal_shop.shop.item.dto.ItemDTOList;
import animal_shop.shop.item.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/seller")
public class SellerController {

    @Autowired
    private ItemService itemService;

    @PostMapping("/item/new")
    public ResponseEntity<?> registerItem(@RequestHeader(value = "Authorization") String token, @RequestBody ItemDTOList itemDTOList) {
        ResponseDTO responseDTO = null;

        try {
            itemService.save(token, itemDTOList);
            responseDTO = ResponseDTO.builder()
                    .message("save success")
                    .build();
            return ResponseEntity.ok().body(responseDTO);

        } catch (Exception e) {
            responseDTO = ResponseDTO.builder()
                    .error(e.getMessage())
                    .build();

            return ResponseEntity.badRequest().body(responseDTO);
        }
    }


    @GetMapping("/mypage")
    public ResponseEntity<?> getItems(
            @RequestHeader(value = "Authorization") String token,
            @RequestBody ItemDTOList itemDTOList) {
        ResponseDTO responseDTO = null;
        try {
            itemService.update(token, itemDTOList);
            responseDTO = ResponseDTO.builder()
                    .message("update success")
                    .build();
            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            responseDTO = ResponseDTO.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @PatchMapping("/item/update")
    public ResponseEntity<?> updateItem(
            @RequestHeader(value = "Authorization") String token,
            @RequestBody ItemDTOList itemDTOList) {
        ResponseDTO responseDTO = null;
        try {
            itemService.update(token, itemDTOList);
            responseDTO = ResponseDTO.builder()
                    .message("update success")
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
