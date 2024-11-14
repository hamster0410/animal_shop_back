package animal_shop.domain.heart_comment.controller;

import animal_shop.domain.heart_comment.service.CommentHeartService;
import animal_shop.global.dto.ResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/comment_heart")
public class CommentHeartController {

    @Autowired
    CommentHeartService commentHeartService;

    @GetMapping("/add/{commentId}")
    public ResponseEntity<?> heartAdd(@RequestHeader("Authorization") String token,@PathVariable("commentId") Long commentId){
        ResponseDTO responseDTO;
        String message;
        try {
            commentHeartService.addHeart(token, commentId);
            message = "heart success";
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
            commentHeartService.deleteHeart(token, commentId);
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
