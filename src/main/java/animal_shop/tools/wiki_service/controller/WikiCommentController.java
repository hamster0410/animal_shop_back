package animal_shop.tools.wiki_service.controller;


import animal_shop.global.dto.ResponseDTO;
import animal_shop.tools.wiki_service.service.WikiCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wiki/comment")
public class WikiCommentController {

    @Autowired
    private WikiCommentService wikiCommentService;


    @PostMapping("/{wikiId}/comments")
    public ResponseEntity<?> addComment(@RequestHeader("Authorization") String token,
                                        @PathVariable Long wikiId,
                                        @RequestBody String content
    ) {
        ResponseDTO responseDTO = null;
        try {
            wikiCommentService.addComment(token, wikiId, content);
            responseDTO = ResponseDTO.builder()
                    .message("success")
                    .build();
            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            responseDTO = ResponseDTO.builder()
                    .message(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{commentId}")
    public ResponseEntity<?> deleteComment(@RequestHeader("Authorization") String token,
                                           @PathVariable Long commentId) {
        try {
            wikiCommentService.deleteComment(token, commentId);
            return ResponseEntity.ok().body("Comment deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}