package animal_shop.tools.abandoned_animal.controller;


import animal_shop.global.dto.ResponseDTO;
import animal_shop.global.security.TokenProvider;
import animal_shop.tools.abandoned_animal.dto.*;
import animal_shop.tools.abandoned_animal.entity.AbandonedComment;
import animal_shop.tools.abandoned_animal.service.AbandonedAnimalService;
import animal_shop.tools.abandoned_animal.service.AbandonedCommnetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/abandoned_animal")
public class AbandonedAnimalController {

    @Autowired
    AbandonedAnimalService abandonedAnimalService;

    @Autowired
    AbandonedCommnetService abandonedCommnetService;

    @Autowired
    TokenProvider tokenProvider;

    @GetMapping("/find")
    public ResponseEntity<?> callForecastApi(){
        return abandonedAnimalService.storeAPIInfo();
    }

//    @GetMapping("/interest-list")
//    public ResponseEntity<?> listInterestAnimal(@RequestHeader(value = "Authorization") String token,
//                                                @RequestParam(value = "page", defaultValue = "1", required = false)int page){
//        try{
//            AnimalListDTOResponse animalListDTOResponse =  abandonedAnimalService.searchInterestInfo(token, page-1);
//            return ResponseEntity.ok().body(animalListDTOResponse);
//        }catch (Exception e){
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//    }
    @PostMapping("/search")
    public ResponseEntity<?> findAnimal(@RequestBody AnimalSearchDTO animalSearchDTO,
                                        @RequestParam(value = "page", defaultValue = "1", required = false)int page){
        try{
            AnimalListDTOResponse animalListDTOResponse =  abandonedAnimalService.searchAPIInfo(animalSearchDTO, page-1);
            return ResponseEntity.ok().body(animalListDTOResponse);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/detail")
    public ResponseEntity<?> detailAnimal(@RequestParam(value = "animalId") Long animalId){
        ResponseDTO responseDTO;
        try{
            AnimalDetailDTO animalDetailDTO =  abandonedAnimalService.searchDetailAPI(animalId);
            return ResponseEntity.ok().body(animalDetailDTO);
        }catch (Exception e){
            responseDTO = ResponseDTO.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }
    // 관심동물 리스트
    @GetMapping("/list-interest")
    public ResponseEntity<?> listInterest(@RequestHeader(value = "Authorization") String token,
                                          @RequestParam(value = "page", required = false, defaultValue = "1")int page) {
        ResponseDTO responseDTO = null;
        try {
            // 관심동물 등록 처리
            InterestDTOResponse interestDTOResponse = abandonedAnimalService.listInterestAnimal(token,page-1);
            // 성공 메시지 반환
            return ResponseEntity.ok().body(interestDTOResponse);
        } catch (Exception e) {
            // 오류 발생 시 메시지 반환
            responseDTO = ResponseDTO.builder()
                    .message(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    // 관심동물 등록
    @GetMapping(value = "/register")
    public ResponseEntity<?> registerInterest(@RequestHeader(value = "Authorization") String token,
                                              @RequestParam(value = "desertionNo")String desertion_no) {
        ResponseDTO responseDTO = null;
        try {
            // 관심동물 등록 처리
            abandonedAnimalService.interestAnimal(token, desertion_no);
            // 성공 메시지 반환
            responseDTO = ResponseDTO.builder()
                    .message("success register")
                    .build();
            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            // 오류 발생 시 메시지 반환
            responseDTO = ResponseDTO.builder()
                    .message(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }
    //관심동물 삭제
    @DeleteMapping("/delete")
    public ResponseEntity<?>deleteInterest(@RequestHeader(value = "Authorization")String token,
                                           @RequestParam(name = "animalId") Long id){
        ResponseDTO responseDTO = null;
        try{
            abandonedAnimalService.indifferentAnimal(token,id);
            responseDTO = ResponseDTO.builder()
                    .message("success delete")
                    .build();
            return ResponseEntity.ok().body(responseDTO);
        }catch (Exception e){
            responseDTO = ResponseDTO.builder()
                    .message(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    //유기동물 게시물에 댓글 달기
    @PostMapping("/{id}/comments")
    public ResponseEntity<?> addAbandonedComments(@PathVariable Long id,
                                                  @RequestHeader(value = "Authorization") String token,
                                                  @RequestBody AbandonedCommentDTO abandonedCommentDTO) {
        ResponseDTO responseDTO = null;
        try{
            AbandonedComment abandonedComment = abandonedCommnetService.addComment(token, id, abandonedCommentDTO.getContent());
            return ResponseEntity.ok().body(abandonedComment);
        } catch (Exception e) {
            responseDTO = ResponseDTO.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }
    @PatchMapping("{id}/comments")
    public ResponseEntity<?> modifyAbandonedComments(@PathVariable Long id,
                                                     @RequestHeader(value="Authorization")String token,
                                                     @RequestBody AbandonedCommentDTO abandonedCommentDTO){
        ResponseDTO responseDTO = null;
        try {
            abandonedCommnetService.modifyComment(token,id,abandonedCommentDTO.getContent());
            responseDTO = ResponseDTO.builder()
                    .message("success register")
                    .build();
            return ResponseEntity.ok().body(responseDTO);
        }catch (Exception e){
            responseDTO = ResponseDTO.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }
    @DeleteMapping("/{id}/comments")
    public ResponseEntity<?> deleteAbandonedComments(@PathVariable Long id,
                                                     @RequestHeader(value="Authorization")String token){
        ResponseDTO responseDTO = null;
        try{
            abandonedCommnetService.deleteComment(token,id);
            responseDTO = ResponseDTO.builder()
                    .message("success delete")
                    .build();
            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            responseDTO = ResponseDTO.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }
    @GetMapping("/findAll")
    public ResponseEntity<?> getAllComments(
            @RequestHeader(value = "Authorization") String token,
            @RequestParam(value = "page", defaultValue = "1") int page) {
        ResponseDTO responseDTO = null;
        try {
            // 모든 댓글 가져오기
            AbandonedCommentDTOResponse response = abandonedCommnetService.getAllComments(page - 1);
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            responseDTO = ResponseDTO.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }
}