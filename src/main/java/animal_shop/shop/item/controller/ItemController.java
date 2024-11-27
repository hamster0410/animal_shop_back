package animal_shop.shop.item.controller;

import animal_shop.global.dto.ResponseDTO;
import animal_shop.shop.item.dto.ItemDetailDTO;
import animal_shop.shop.item.dto.QueryResponse;
import animal_shop.shop.item.dto.RequestItemQueryDTO;
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
    public ResponseEntity<?> item_detail(@PathVariable(value = "itemId") String itemId) {
        ResponseDTO responseDTO = null;
        try {
            ItemDetailDTO itemDetailDTO = itemService.findById(itemId);
            return ResponseEntity.ok().body(itemDetailDTO);
        } catch (Exception e) {
            responseDTO = ResponseDTO.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @PostMapping("/query/new")
    public ResponseEntity<?> item_query_new(
            @RequestHeader(value = "Authorization") String token,
            @RequestBody RequestItemQueryDTO requestItemQueryDTO) {
        ResponseDTO responseDTO = null;
        try {
            itemService.register_enquery(token, requestItemQueryDTO);
            return ResponseEntity.ok().body(requestItemQueryDTO);
        } catch (Exception e) {
            responseDTO = ResponseDTO.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }
    @DeleteMapping("/query/delete/{questionId}")
    public ResponseEntity<?> item_delete(
            @RequestHeader(value = "Authorization") String token,
            @PathVariable(value = "questionId")String questionId){

        ResponseDTO responseDTO = null;
        try{
            itemService.delete_query(token,questionId);
            responseDTO = ResponseDTO.builder()
                    .message("delete success!")
                    .build();
            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            responseDTO = ResponseDTO.builder()
                    .error(e.getMessage())
                    .build();
            return  ResponseEntity.badRequest().body(responseDTO);
        }
    }
    @GetMapping("/query/list/{item_id}")
    public ResponseEntity<?> search_query(
            @RequestHeader(value = "Authorization",required = false)String token,
            @PathVariable(value = "item_id")String itemId,
            @RequestParam(value = "page", defaultValue = "1")int page){
        ResponseDTO responseDTO = null;
        try{
            QueryResponse queryResponse = itemService.select_query(token, itemId, page-1);
            return ResponseEntity.ok().body(queryResponse);
        } catch (Exception e) {
            responseDTO = ResponseDTO.builder()
                    .message(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }
    @GetMapping("/search")
    public ResponseEntity<?>search_item(@RequestHeader(value = "Authorization",required = false)String token,
                                        @RequestParam(value = "searchTerm",required = false)String searchTerm,
                                        @RequestParam(value = "sellerName",required = false)String sellerName,
                                        @RequestParam(value = "page",defaultValue = "1")int page){
        ResponseDTO responseDTO = null;
        try {
            QueryResponse queryResponse = itemService.search_item(token, searchTerm,sellerName,page-1);
            return ResponseEntity.ok().body(queryResponse);
        }catch (Exception e){
            responseDTO = ResponseDTO.builder()
                    .message(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }


}

