package animal_shop.domain.comment.controller;

import animal_shop.domain.comment.dto.CommentDTO;
import animal_shop.domain.comment.dto.CommentResponseDTO;
import animal_shop.domain.comment.dto.RequestCommentDTO;
import animal_shop.domain.comment.service.CommentService;
import animal_shop.global.dto.ResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    CommentService commentService;

    @GetMapping("/{postId}")
    public ResponseEntity<?> getCommentsBy(@RequestHeader(value = "Authorization", required = false) String token,
                                           @PathVariable("postId") Long postId) {
        // 댓글 조회 로직
        try{
            CommentResponseDTO commentResponseDTO = commentService.getCommentsByPostId(postId,token);

            return ResponseEntity.ok().body(commentResponseDTO);
        }catch(Exception e){
            ResponseDTO responseDTO = ResponseDTO.builder()
                    .error(e.getMessage()).build();
            return ResponseEntity
                    .badRequest()
                    .body(responseDTO);
        }
    }

    @PostMapping(value = "/create/{postId}", consumes = "multipart/form-data")
    public ResponseEntity<?> createComment(
            @RequestHeader("Authorization") String token,
            @PathVariable("postId") Long postId,
            @RequestPart("commentData") RequestCommentDTO requestCommentDTO,
            @RequestPart(value = "imageFile", required = false) List<MultipartFile> imageFiles) {

        ResponseDTO responseDTO;
        // imageFiles가 null인지 확인하고 null이면 빈 리스트로 초기화

         // 댓글 생성 로직
        try{
             commentService.createComment(token,postId,requestCommentDTO,imageFiles);

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
            boolean writer_check = commentService.checkCommentWriter(token ,commentId);

            return ResponseEntity.ok(writer_check);
        }catch (Exception e){
            ResponseDTO responseDTO = ResponseDTO.builder()
                    .error(e.getMessage()).build();
            return ResponseEntity
                    .badRequest()
                    .body(responseDTO);
        }
    }


    @PutMapping(value = "/update/{commentId}", consumes = "multipart/form-data")
    public ResponseEntity<?> updateComment(@RequestHeader("Authorization") String token ,
                                           @PathVariable("commentId") Long commentId,
                                           @RequestPart("commentData") RequestCommentDTO requestCommentDTO,
                                           @RequestPart(value = "imageFile", required = false) List<MultipartFile> imageFiles)
    {
        try{
            CommentDTO commentReturnDTO = commentService.updateComment(token,commentId,requestCommentDTO, imageFiles);

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
            commentService.deleteComment(token, commentId);
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
