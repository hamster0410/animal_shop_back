package animal_shop.tools.wiki_service.controller;

import animal_shop.tools.wiki_service.dto.WikiDTO;
import animal_shop.tools.wiki_service.dto.WikiDTOResponse;
import animal_shop.tools.wiki_service.entity.Wiki;
import animal_shop.tools.wiki_service.service.WikiService;
import animal_shop.global.dto.ResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/wiki")
public class WikiController {

    @Autowired
    private WikiService wikiService;

    //등록
    @PostMapping(value = "/register")
    public ResponseEntity<?> wiki_register(@RequestHeader(value = "Authorization") String token,
                                           @RequestBody WikiDTO wikiDTO) {
        ResponseDTO responseDTO = null;
        try {
            wikiService.wikiRegister(token, wikiDTO);
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

    @GetMapping("/select")
    ResponseEntity<?> wiki_select(@RequestHeader(value = "Authorization",required = false) String token,
                                  @RequestParam(value = "page", required = false, defaultValue = "1") int page) {
        ResponseDTO responseDTO = null;
        try {
            WikiDTOResponse wikiDTOResponse = wikiService.select(token, page - 1);
            return ResponseEntity.ok().body(wikiDTOResponse);
        } catch (Exception e) {
            responseDTO = ResponseDTO.builder()
                    .message(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @GetMapping("/select/{id}")
    public ResponseEntity<?> select_breed(@RequestHeader(value = "Authorization",required = false) String token,
                                          @PathVariable Long id) {
        ResponseDTO responseDTO = null;
        try {
            WikiDTO wikiDTO = new WikiDTO();
            wikiDTO.setId(id);
            WikiDTOResponse wikiDTOResponse = wikiService.selectDetail(token, wikiDTO);
            return ResponseEntity.ok().body(wikiDTOResponse);
        } catch (Exception e) {
            responseDTO = ResponseDTO.builder()
                    .message(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @DeleteMapping("/delete/{breedId}")
    ResponseEntity<?> wiki_delete(@RequestHeader(value = "Authorization") String token,
                                  @PathVariable Long breedId) {
        ResponseDTO responseDTO = null;
        try {
            WikiDTO wikiDTO = new WikiDTO();
            wikiDTO.setId(breedId);
            wikiService.delete(token, wikiDTO);
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
    //수정
    @PatchMapping("/update/{breedId}")
    public ResponseEntity<?>wiki_update(@RequestHeader(value = "Authorization")String token,
                                 @PathVariable Long breedId,
                                 @RequestBody WikiDTO wikiDTO){

        ResponseDTO responseDTO = null;
        try {
            wikiDTO.setId(breedId);
            wikiService.update(token,wikiDTO);
            responseDTO = ResponseDTO.builder()
                    .message("success update")
                    .build();
            return ResponseEntity.ok().body(responseDTO);
        }catch (Exception e ){
            responseDTO = ResponseDTO.builder()
                    .message(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }
}