package animal_shop.shop.item.controller;

import animal_shop.global.dto.ResponseDTO;
import animal_shop.shop.item.dto.ItemDTOList;
import animal_shop.shop.item.dto.ItemDTOListResponse;
import animal_shop.shop.item.dto.ItemDetailDTO;
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

    //상품 마이페이지에 뜰려면 보내야 하니까
    @GetMapping("/item/update")
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

    //상품 업데이트
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

    //상품 삭제
    @DeleteMapping("/item/delete/{itemId}")
    public ResponseEntity<?> deleteItem(
            @RequestHeader(value = "Authorization") String token,
            @PathVariable(value = "itemId") String id) {
        ResponseDTO responseDTO = null;
        try {
            itemService.delete(token, id);
            responseDTO = ResponseDTO.builder()
                    .message("delete success")
                    .build();
            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            responseDTO = ResponseDTO.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    //개별 상품 조회
    @GetMapping("/item/select/{itemId}")
    public ResponseEntity<?> selectItem(
            @RequestHeader(value = "Authorization") String token,
            @PathVariable(value = "itemId") String id) {
        ResponseDTO responseDTO = null;
        try {
            ItemDetailDTO  itemDetailDTO = itemService.selectItem(token, id);

            return ResponseEntity.ok().body(itemDetailDTO);
        } catch (Exception e) {
            responseDTO = ResponseDTO.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    //전체 상품 조회
    @GetMapping("/item/select")
    public ResponseEntity<?> selectAll(@RequestHeader(value = "Authorization") String token,
                                       @RequestParam(value = "page" , defaultValue = "1") int page) {
        ResponseDTO responseDTO;
        try {
            ItemDTOListResponse items = itemService.selectAll(token,page-1);
            return ResponseEntity.ok().body(items);
        } catch (IllegalArgumentException | IllegalStateException e) {
            responseDTO = ResponseDTO.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        } catch (Exception e) {
            responseDTO = ResponseDTO.builder()
                    .error("An unexpected error occurred")
                    .build();
            return ResponseEntity.internalServerError().body(responseDTO);
        }
    }

}