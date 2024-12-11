package animal_shop.tools.Like.controller;

import animal_shop.global.dto.ResponseDTO;
import animal_shop.tools.Like.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/like")
public class LikeController {

    @Autowired
    private LikeService likeService;

    @GetMapping("/add/{mapId}")
    public ResponseEntity<?> addLike(@RequestHeader("Authorization")String token,
                                     @PathVariable ("mapId")Long mapId){
        ResponseDTO responseDTO = null;

        try {
            likeService.addLike(token,mapId);
            responseDTO = ResponseDTO.builder()
                    .message("like")
                    .build();
            return ResponseEntity.ok().body(responseDTO);
        }catch (Exception e){
            responseDTO = ResponseDTO.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }
    @GetMapping("delete/{mapId}")
    public ResponseEntity<?> deleteLike(@RequestHeader("Authorization")String token,
                                        @PathVariable("mapId")Long mapId){
        ResponseDTO responseDTO = null;

        try {
            likeService.deleteLike(token,mapId);
            responseDTO = ResponseDTO.builder()
                    .message("delete like")
                    .build();
            return ResponseEntity.ok().body(responseDTO);
        }catch (Exception e){
            responseDTO = ResponseDTO.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

}
