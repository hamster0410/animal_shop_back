package animal_shop.domain.heart_post.controller;

import animal_shop.domain.heart_post.service.HeartService;
import animal_shop.global.dto.ResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/heart")
public class HeartController {

    @Autowired
    HeartService heartService;

    @GetMapping("/add/{postId}")
    public ResponseEntity<?> heartAdd(@RequestHeader("Authorization") String token,@PathVariable("postId") Long postId){
        ResponseDTO responseDTO;
        String message;
        try {
            heartService.addHeart(token, postId);
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

    @GetMapping("/delete/{postId}")
    public ResponseEntity<?> heartDelete(@RequestHeader("Authorization") String token,@PathVariable("postId")Long postId){
        ResponseDTO responseDTO;
        String message;
        try {
            heartService.deleteHeart(token, postId);
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
