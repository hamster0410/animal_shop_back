package animal_shop.community.post.controller;
import animal_shop.community.post.dto.NoticeDTOResponse;
import animal_shop.community.post.dto.NoticesDTO;
import animal_shop.community.post.service.NoticesService;
import animal_shop.global.dto.ResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/notices")
public class NoticesController {

    @Autowired
    private NoticesService noticesService;

    // 관리자 공지사항 ?  등록
    @PostMapping(value = "/register",consumes = "multipart/form-data")
    ResponseEntity<?> admin_register(
            @RequestHeader(value = "Authorization")String token,
            @RequestPart(value = "file", required = false)MultipartFile file,
            @RequestPart NoticesDTO noticesDTO){
        ResponseDTO responseDTO = null;
        try {
            noticesService.admin_register(token,noticesDTO,file);
            responseDTO = ResponseDTO.builder()
                    .message("success register")
                    .build();
            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            responseDTO = ResponseDTO.builder()
                    .message(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }
    //전체조회
    @GetMapping("/select")
    ResponseEntity<?>admin_search(@RequestHeader(value = "Authorization")String token,
                                  @RequestParam(value = "page", required = false, defaultValue = "1")int page){
        ResponseDTO responseDTO = null;
        try{
            NoticeDTOResponse noticeDTOResponse =  noticesService.adminSearch(token, page - 1);

            return ResponseEntity.ok().body(noticeDTOResponse);
        } catch (Exception e) {
            responseDTO = ResponseDTO.builder()
                    .message(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    //개별 조회
    @GetMapping("/select/{noticeId}")
    public ResponseEntity<?> admin_searchId(@RequestHeader(value = "Authorization") String token,
                                            @PathVariable Long noticeId) {
        ResponseDTO responseDTO = null;
        try {
            NoticesDTO noticesDTO = new NoticesDTO();
            noticesDTO.setId(noticeId);  // noticeId를 DTO에 설정

            NoticeDTOResponse noticeDTOResponse = noticesService.adminSearchId(token, noticesDTO);
            return ResponseEntity.ok().body(noticeDTOResponse);
        } catch (Exception e) {
            responseDTO = ResponseDTO.builder()
                    .message(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    //공지사항 삭제
    @DeleteMapping("/delete/{noticeId}")
    public ResponseEntity<?> admin_delete(@RequestHeader(value = "Authorization")String token,
                                          @PathVariable Long noticeId){
        ResponseDTO responseDTO = null;
        try {
            NoticesDTO noticesDTO = new NoticesDTO();
            noticesDTO.setId(noticeId);  // noticeId를 DTO에 설정

            noticesService.delete(token,noticesDTO);
            responseDTO = ResponseDTO.builder()
                    .message("success Delete")
                    .build();
            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            responseDTO = ResponseDTO.builder()
                    .message(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @PatchMapping("/update/{noticeId}")
    public ResponseEntity<?> admin_update(@RequestHeader(value = "Authorization") String token,
                                          @PathVariable Long noticeId, // noticeId를 PathVariable로 받기
                                          @RequestPart(required = false) NoticesDTO noticesDTO,
                                          @RequestPart(value = "file", required = false) MultipartFile file) { // @RequestBody로 DTO 받기
        ResponseDTO responseDTO = null;
        try {
            // noticesDTO에 noticeId를 설정
            noticesDTO.setId(noticeId);

            // 공지사항 업데이트 수행
            noticesService.update(token, noticesDTO,file);

            // 성공 응답
            responseDTO = ResponseDTO.builder()
                    .message("success update")
                    .build();
            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            // 예외 발생 시 실패 응답
            responseDTO = ResponseDTO.builder()
                    .message(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

}
