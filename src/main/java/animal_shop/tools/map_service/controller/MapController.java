package animal_shop.tools.map_service.controller;


import animal_shop.global.dto.ResponseDTO;
import animal_shop.tools.map_service.dto.MapCommentDTO;
import animal_shop.tools.map_service.dto.MapCommentDTOResponse;
import animal_shop.tools.map_service.dto.MapDetailDTO;
import animal_shop.tools.map_service.dto.MapPositionDTOResponse;
import animal_shop.tools.map_service.dto.SearchRequestDTO;
import animal_shop.tools.map_service.service.MapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/map")
public class MapController {

    @Autowired
    private MapService mapService;

    @GetMapping("/find")
    public ResponseEntity<?> find_home() {
        ResponseDTO responseDTO = null;
        try {
            mapService.mapFind();
            responseDTO = ResponseDTO.builder()
                    .message("success find")
                    .build();
            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            responseDTO = ResponseDTO.builder()
                    .message(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

          
    @PostMapping("/search")
    public ResponseEntity<?> search_position(@RequestHeader(value = "Authorization")String token,
                                             @RequestBody SearchRequestDTO searchRequestDTO,
                                             @RequestParam(name = "page", required = false, defaultValue = "1")int page){
        ResponseDTO responseDTO = null;

        try {
            MapPositionDTOResponse mapPositionDTOResponse = mapService.search(token,searchRequestDTO, page-1);
            return ResponseEntity.ok().body(mapPositionDTOResponse);
        }catch (Exception e){
            responseDTO = ResponseDTO.builder()
                    .message(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }
  
    @PostMapping("/detail")
    public ResponseEntity<?> detail(@RequestHeader(value = "Authorization")String token,
                                    @RequestParam(name = "mapId") long map_id){
        ResponseDTO responseDTO = null;

        try {
            MapDetailDTO mapDetailDTO = mapService.detail(token,map_id);
            return ResponseEntity.ok().body(mapDetailDTO);
        }catch (Exception e){
            responseDTO = ResponseDTO.builder()
                    .message(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }
  
    //댓글 생성
    @PostMapping("/comment/register")
    public ResponseEntity<?> createMapComment(@RequestHeader("Authorization") String token,
                                              @RequestBody MapCommentDTO mapCommentDTO) {
        ResponseDTO responseDTO = null;
        try {
            mapService.createMapComment(token, mapCommentDTO);
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


    @Transactional(readOnly = true)
    @GetMapping("/comment/check")
    public ResponseEntity<?> selectMapComment(@RequestHeader("Authorization") String token,
                                              @RequestParam(name = "commentId") long comment_id) {

        ResponseDTO responseDTO = null;
        try {
            boolean check = mapService.checkMapComment(token,comment_id);
            return ResponseEntity.ok().body(check);
        } catch (Exception e) {
            responseDTO = ResponseDTO.builder()
                    .message(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @Transactional(readOnly = true)
    @GetMapping("/comment/select")
    public ResponseEntity<?> selectMapComment(@RequestHeader("Authorization") String token,
                                              @RequestParam(name = "mapId") long map_id,
                                              @RequestParam(value = "page", defaultValue = "1") int page) {

        ResponseDTO responseDTO = null;
        try {
            MapCommentDTOResponse mapCommentDTOResponse = mapService.selectMapComment(token, map_id, page - 1);
            return ResponseEntity.ok().body(mapCommentDTOResponse);
        } catch (Exception e) {
            responseDTO = ResponseDTO.builder()
                    .message(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @PatchMapping("/comment/update")
    public ResponseEntity<?> updateMapComment(@RequestHeader("Authorization") String token,
                                              @RequestBody MapCommentDTO mapCommentDTO) {
        ResponseDTO responseDTO = null;
        try {
            mapService.updateMapComment(token, mapCommentDTO);
            responseDTO = ResponseDTO.builder()
                    .message("success update")
                    .build();
            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
             responseDTO = ResponseDTO.builder()
                    .message(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @DeleteMapping("/comment/delete")
    public ResponseEntity<?> deleteMapComment(@RequestHeader("Authorization") String token,
                                              @RequestParam(name = "commentId") long comment_id
    ) {
        ResponseDTO responseDTO = null;
        try {
            mapService.deleteMapComment(token, comment_id);
            responseDTO = ResponseDTO.builder()
                    .message("success delete")
                    .build();
            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            responseDTO = ResponseDTO.builder()
                    .message(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }
}




