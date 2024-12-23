package animal_shop.community.member.controller;

import animal_shop.community.comment.dto.CommentResponseDTO;
import animal_shop.community.member.dto.MemberDTO;
import animal_shop.community.member.dto.MyPageDTO;
import animal_shop.community.member.dto.SellerRegisterDTO;
import animal_shop.community.member.service.MemberService;
import animal_shop.community.post.dto.PostResponseDTO;
import animal_shop.global.dto.ResponseDTO;
import animal_shop.shop.item.dto.QueryResponse;
import animal_shop.shop.item_comment.dto.ItemCommentDTOResponse;
import animal_shop.tools.abandoned_animal.dto.AbandonedCommentDTOResponse;
import animal_shop.tools.map_service.dto.MapCommentDTOResponse;
import animal_shop.tools.map_service.dto.MapPositionDTOResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/mypage")
public class MyPageController {

    @Autowired
    private MemberService memberService;

    @GetMapping("/update")
    public ResponseEntity<?> memberInfo(@RequestHeader("Authorization") String token){
        try{
            MyPageDTO myPageDTO = memberService.getByToken(token);
            return ResponseEntity.ok().body(myPageDTO);
        }catch (Exception e){
            log.error("mypage failed  error: {}", e.getMessage());

            ResponseDTO responseDTO = ResponseDTO.builder()
                    .error("mypage failed.")
                    .build();

            return ResponseEntity
                    .badRequest()
                    .body(responseDTO);
        }
    }

    @PostMapping("/update")
    public ResponseEntity<?> memberModify(@RequestHeader("Authorization") String token, @RequestBody MemberDTO memberDTO){
        ResponseDTO responseDTO;
        try{
            memberService.modify(memberDTO, token);
            responseDTO = ResponseDTO.builder().message("modify success").build();
            return ResponseEntity.ok().body(responseDTO);

        }catch(Exception e){
            responseDTO = ResponseDTO.builder()
                    .error(e.getMessage()).build();
            return ResponseEntity
                    .badRequest()
                    .body(responseDTO);
        }
    }
    @PostMapping("/seller-register")
    public ResponseEntity<?> sellerRegister(@RequestHeader("Authorization") String token, @RequestBody SellerRegisterDTO sellerRegisterDTO){
        ResponseDTO responseDTO;

        try{
            memberService.enroll_seller(token,sellerRegisterDTO);
            responseDTO = ResponseDTO.builder().message("enroll success").build();
            return ResponseEntity.ok().body(responseDTO);

        }catch(Exception e){
            responseDTO = ResponseDTO.builder()
                    .error(e.getMessage()).build();
            return ResponseEntity
                    .badRequest()
                    .body(responseDTO);
        }
    }

    @GetMapping("/mypost")
    public ResponseEntity<?> mypost(@RequestHeader("Authorization") String token,
                                       @RequestParam(name = "page", defaultValue = "1", required = false)int page){
        ResponseDTO responseDTO;

        try{
            PostResponseDTO postResponseDTO = memberService.myPost(token,page-1);

            return ResponseEntity.ok().body(postResponseDTO);

        }catch(Exception e){
            responseDTO = ResponseDTO.builder()
                    .error(e.getMessage()).build();
            return ResponseEntity
                    .badRequest()
                    .body(responseDTO);
        }
    }

    @GetMapping("/mycomment")
    public ResponseEntity<?> mycomment(@RequestHeader("Authorization") String token,
                                       @RequestParam(name = "page", defaultValue = "1", required = false)int page){
        ResponseDTO responseDTO;

        try{
            CommentResponseDTO commentResponseDTO = memberService.myComment(token,page-1);

            return ResponseEntity.ok().body(commentResponseDTO);

        }catch(Exception e){
            responseDTO = ResponseDTO.builder()
                    .error(e.getMessage()).build();
            return ResponseEntity
                    .badRequest()
                    .body(responseDTO);
        }
    }

    @GetMapping("/mycomment-animal")
    public ResponseEntity<?> mycommentAnimal(@RequestHeader("Authorization") String token,
                                    @RequestParam(name = "page", defaultValue = "1", required = false)int page){
        ResponseDTO responseDTO;

        try{
            AbandonedCommentDTOResponse abandonedCommentDTOResponse = memberService.myAnimalComment(token,page-1);

            return ResponseEntity.ok().body(abandonedCommentDTOResponse);

        }catch(Exception e){
            responseDTO = ResponseDTO.builder()
                    .error(e.getMessage()).build();
            return ResponseEntity
                    .badRequest()
                    .body(responseDTO);
        }
    }

    @GetMapping("/myquery")
    public ResponseEntity<?> myquery(@RequestHeader("Authorization") String token,
                                       @RequestParam(name = "page", defaultValue = "1", required = false)int page) {
        ResponseDTO responseDTO;

        try {
            QueryResponse queryResponse = memberService.myQuery(token, page - 1);

            return ResponseEntity.ok().body(queryResponse);

        } catch (Exception e) {
            responseDTO = ResponseDTO.builder()
                    .error(e.getMessage()).build();
            return ResponseEntity
                    .badRequest()
                    .body(responseDTO);
        }
    }

    @GetMapping("/myreview")
    public ResponseEntity<?> myreview(@RequestHeader("Authorization") String token,
                                     @RequestParam(name = "page", defaultValue = "1", required = false)int page) {
        ResponseDTO responseDTO;

        try {
            ItemCommentDTOResponse itemCommentDTOResponse = memberService.myReview(token, page - 1);

            return ResponseEntity.ok().body(itemCommentDTOResponse);

        } catch (Exception e) {
            responseDTO = ResponseDTO.builder()
                    .error(e.getMessage()).build();
            return ResponseEntity
                    .badRequest()
                    .body(responseDTO);
        }
    }

    @GetMapping("/myplace-review")
    public ResponseEntity<?> placeMyreview(@RequestHeader("Authorization") String token,
                                      @RequestParam(name = "page", defaultValue = "1", required = false)int page) {
        ResponseDTO responseDTO;

        try {
            MapCommentDTOResponse mapCommentDTOResponse = memberService.myPlaceReview(token, page - 1);

            return ResponseEntity.ok().body(mapCommentDTOResponse);

        } catch (Exception e) {
            responseDTO = ResponseDTO.builder()
                    .error(e.getMessage()).build();
            return ResponseEntity
                    .badRequest()
                    .body(responseDTO);
        }
    }

    @GetMapping("/like-post")
    public ResponseEntity<?> likePost(@RequestHeader("Authorization") String token,
                                    @RequestParam(name = "page", defaultValue = "1", required = false)int page){
        ResponseDTO responseDTO;

        try{
            PostResponseDTO postResponseDTO = memberService.likePost(token,page-1);

            return ResponseEntity.ok().body(postResponseDTO);

        }catch(Exception e){
            responseDTO = ResponseDTO.builder()
                    .error(e.getMessage()).build();
            return ResponseEntity
                    .badRequest()
                    .body(responseDTO);
        }
    }

    @GetMapping("/like-comment")
    public ResponseEntity<?> likeComment(@RequestHeader("Authorization") String token,
                                       @RequestParam(name = "page", defaultValue = "1", required = false)int page){
        ResponseDTO responseDTO;

        try{
            CommentResponseDTO commentResponseDTO = memberService.likeComment(token,page-1);

            return ResponseEntity.ok().body(commentResponseDTO);

        }catch(Exception e){
            responseDTO = ResponseDTO.builder()
                    .error(e.getMessage()).build();
            return ResponseEntity
                    .badRequest()
                    .body(responseDTO);
        }
    }

    @GetMapping("/like-review")
    public ResponseEntity<?> likeReview(@RequestHeader("Authorization") String token,
                                      @RequestParam(name = "page", defaultValue = "1", required = false)int page) {
        ResponseDTO responseDTO;

        try {
            ItemCommentDTOResponse itemCommentDTOResponse = memberService.likeReview(token, page - 1);

            return ResponseEntity.ok().body(itemCommentDTOResponse);

        } catch (Exception e) {
            responseDTO = ResponseDTO.builder()
                    .error(e.getMessage()).build();
            return ResponseEntity
                    .badRequest()
                    .body(responseDTO);
        }
    }

    @GetMapping("/like-place")
    public ResponseEntity<?> likePlace(@RequestHeader("Authorization") String token,
                                       @RequestParam(name = "page", defaultValue = "1", required = false)int page){
        ResponseDTO responseDTO;

        try{
            MapPositionDTOResponse mapPositionDTOResponse = memberService.likePlace(token,page-1);

            return ResponseEntity.ok().body(mapPositionDTOResponse);

        }catch(Exception e){
            responseDTO = ResponseDTO.builder()
                    .error(e.getMessage()).build();
            return ResponseEntity
                    .badRequest()
                    .body(responseDTO);
        }
    }


}
