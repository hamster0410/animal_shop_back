package animal_shop.shop.item_comment.controller;

import animal_shop.global.dto.ResponseDTO;
import animal_shop.shop.item_comment.dto.ItemCommentDTO;
import animal_shop.shop.item_comment.dto.ItemCommentDTOResponse;
import animal_shop.shop.item_comment.dto.RequsetItemCommentDTO;
import animal_shop.shop.item_comment.service.ItemCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/item_comment")
public class ItemCommentController {

    @Autowired
    ItemCommentService itemCommentService;

    @GetMapping("/{itemId}")
    public ResponseEntity<?> getCommentsBy(@RequestHeader(value = "Authorization", required = false) String token,
                                           @PathVariable("itemId") Long itemId,
                                           @RequestParam(value = "page", defaultValue = "1") int page,
                                           @RequestParam(value = "about",required = false) String about){

        // 댓글 조회 로직
        try{
            ItemCommentDTOResponse commentResponseDTO = itemCommentService.getCommentsByItemId(itemId,token, page-1,about);

            return ResponseEntity.ok().body(commentResponseDTO);
        }catch(Exception e){
            ResponseDTO responseDTO = ResponseDTO.builder()
                    .error(e.getMessage()).build();
            return ResponseEntity
                    .badRequest()
                    .body(responseDTO);
        }
    }

    @PostMapping(value = "/create/{itemId}")
    public ResponseEntity<?> createComment(@RequestHeader("Authorization") String token,
                                           @PathVariable("itemId") Long itemId,
                                           @RequestBody RequsetItemCommentDTO requestItemCommentDTO){

        ResponseDTO responseDTO;
        // imageFiles가 null인지 확인하고 null이면 빈 리스트로 초기화

        // 댓글 생성 로직
        try{
            itemCommentService.createComment(token,itemId,requestItemCommentDTO);

            responseDTO =ResponseDTO.builder()
                    .message("comment success")
                    .build();
            return ResponseEntity.ok().body(responseDTO);
        }catch (Exception e){
            responseDTO = ResponseDTO
                    .builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }

    }

    @GetMapping(value="/update/{commentId}")
    public ResponseEntity<?> checkingWriter(@RequestHeader("Authorization") String token ,
                                            @PathVariable("commentId") Long commentId){
        try{
            boolean writer_check = itemCommentService.checkCommentWriter(token ,commentId);

            return ResponseEntity.ok(writer_check);
        }catch (Exception e){
            ResponseDTO responseDTO = ResponseDTO.builder()
                    .error(e.getMessage()).build();
            return ResponseEntity
                    .badRequest()
                    .body(responseDTO);
        }
    }

    @PatchMapping(value = "/update/{commentId}")
    public ResponseEntity<?> updateComment(@RequestHeader("Authorization") String token ,
                                           @PathVariable("commentId") Long commentId,
                                           @RequestBody RequsetItemCommentDTO requestItemCommentDTO)
    {
        try{
            ItemCommentDTO commentReturnDTO = itemCommentService.updateComment(token,commentId,requestItemCommentDTO);

            return ResponseEntity.ok(commentReturnDTO);
        }catch(Exception e){
            ResponseDTO responseDTO = ResponseDTO.builder()
                    .error(e.getMessage()).build();
            return ResponseEntity
                    .badRequest()
                    .body(responseDTO);
        }
    }



    @DeleteMapping("/delete/{commentId}")
    public ResponseEntity<?> deleteComment(@RequestHeader("Authorization") String token, @PathVariable("commentId") Long commentId) {
        ResponseDTO responseDTO;
        try {
            itemCommentService.deleteComment(token, commentId);
            responseDTO = ResponseDTO.builder().
                    message("delete success")
                    .build();
            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            responseDTO = ResponseDTO.builder()
                    .error(e.getMessage()).build();
            return ResponseEntity
                    .badRequest()
                    .body(responseDTO);
        }
    }
}
