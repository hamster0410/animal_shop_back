package animal_shop.shop.item_comment_like.controller;

import animal_shop.global.dto.ResponseDTO;
import animal_shop.shop.item_comment_like.service.ItemCommentLikeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/item_comment_like")
public class ItemCommentLikeController {
    @Autowired
    ItemCommentLikeService itemCommentLikeService;

    @GetMapping("/add/{commentId}")
    public ResponseEntity<?> heartAdd(@RequestHeader("Authorization") String token,@PathVariable("commentId") Long commentId){
        ResponseDTO responseDTO;
        String message;
        try {
            itemCommentLikeService.addHeart(token, commentId);
            message = "like success";
            responseDTO = ResponseDTO.builder()
                    .message(message)
                    .build();
            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            responseDTO = ResponseDTO.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);

        }
    }

    @GetMapping("/delete/{commentId}")
    public ResponseEntity<?> heartDelete(@RequestHeader("Authorization") String token,@PathVariable("commentId")Long commentId){
        ResponseDTO responseDTO;
        String message;
        try {
            itemCommentLikeService.deleteHeart(token, commentId);
            message = "delete success";
            responseDTO = ResponseDTO.builder()
                    .message(message)
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
