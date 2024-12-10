package animal_shop.tools.map_service.controller;


import animal_shop.global.dto.ResponseDTO;
import animal_shop.tools.map_service.dto.MapCommentDTO;
import animal_shop.tools.map_service.dto.MapCommentDTOResponse;
import animal_shop.tools.map_service.dto.MapDTO;
import animal_shop.tools.map_service.dto.MapDTOResponse;
import animal_shop.tools.map_service.service.MapService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.UUID;

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


    //댓글 생성
    @PostMapping("/register")
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

    @PatchMapping("/update")
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

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteMapComment(@RequestHeader("Authorization") String token,
                                              @RequestBody MapCommentDTO mapCommentDTO
    ) {
        ResponseDTO responseDTO = null;
        try {
            mapService.deleteMapComment(token, mapCommentDTO);
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

    @Transactional(readOnly = true)
    @GetMapping("/select")
    public ResponseEntity<?> selectMapComment(@RequestHeader("Authorization") String token,
                                              @RequestBody MapCommentDTO mapCommentDTO,
                                              @RequestParam(value = "page", defaultValue = "1") int page) {

        ResponseDTO responseDTO = null;
        try {
            MapCommentDTOResponse mapCommentDTOResponse = mapService.selectMapComment(token, mapCommentDTO, page - 1);
            return ResponseEntity.ok().body(mapCommentDTOResponse);
        } catch (Exception e) {
            responseDTO = ResponseDTO.builder()
                    .message(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }
}




