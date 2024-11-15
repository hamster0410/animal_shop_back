package animal_shop.community.post.controller;


import animal_shop.community.heart_post.service.HeartService;
import animal_shop.community.post.dto.*;
import animal_shop.community.post.service.PostService;
import animal_shop.global.dto.ResponseDTO;
import animal_shop.global.security.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class PostApiController {

    @Autowired
    private  PostService postService;

    @Autowired
    private HeartService heartService;

    @Autowired
    private TokenProvider tokenProvider;

    @GetMapping({"/", "/best"})
    public ResponseEntity<?> bestList(@RequestParam(value = "page", defaultValue = "1") int pageId) {
        try {
            System.out.println("postapicontroller start");
            List<PostListDTO> paging = postService.getBestPost(pageId - 1);
            Long postCount = postService.getPostCount();
            PostResponseDTO postResponseDTO = PostResponseDTO
                    .builder()
                    .posts(paging)
                    .totalCount(postCount)
                    .build();
            return ResponseEntity.ok().body(postResponseDTO);
        } catch (Exception e) {
            ResponseDTO responseDTO = ResponseDTO.builder()
                    .error("best_list fail").build();
            return ResponseEntity
                    .badRequest()
                    .body(responseDTO);
        }
    }


    @GetMapping("/new")
    public ResponseEntity<?> main_list(@RequestParam(value = "page", defaultValue = "1") int pageid){
        try{
            List<PostListDTO> paging = postService.getNewPosts(pageid-1);
            Long postCount = postService.getPostCount();
            PostResponseDTO postResponseDTO = PostResponseDTO
                    .builder()
                    .posts(paging)
                    .totalCount(postCount)
                    .build();
            return ResponseEntity.ok().body(postResponseDTO);
        }catch(Exception e){
            ResponseDTO responseDTO = ResponseDTO.builder()
                    .error("main_list fail").build();
            return ResponseEntity
                    .badRequest()
                    .body(responseDTO);
        }
    }

    @GetMapping("/{category}")
    public ResponseEntity<?> main_list(@PathVariable("category") String category,@RequestParam(value = "page", defaultValue = "1") int pageid){
        try{
            PostResponseDTO postResponseDTO = postService.getCategoryPosts(pageid-1, category);

            return ResponseEntity.ok().body(postResponseDTO);
        }catch(Exception e){
            ResponseDTO responseDTO = ResponseDTO.builder()
                    .error("main_list fail").build();
            return ResponseEntity
                    .badRequest()
                    .body(responseDTO);
        }
    }

    //게시글 검색
    @GetMapping("/search/{about}")
    public ResponseEntity<?> postSearch(@PathVariable("about") String about,
                                        @RequestParam("keyword") String keyword,
                                        @RequestParam(value = "page", defaultValue = "1") int pageid){
        ResponseDTO responseDTO;
        try{
            System.out.println(about + " " + keyword + " " + pageid + " here ");
            PostResponseDTO postResponseDTO = postService.getSearchPost(about, keyword, pageid-1);
            return ResponseEntity.ok().body(postResponseDTO);
        }catch(Exception e){
            responseDTO = ResponseDTO.builder()
                    .error(e.getMessage()).build();
            return ResponseEntity
                    .badRequest()
                    .body(responseDTO);
        }
    }
    //게시글 상세정보 조회
    @GetMapping("/{category}/{post_id}")
    public ResponseEntity<?> getPostById(@RequestHeader(value = "Authorization", required = false) String token,
            @PathVariable("category") String category,
            @PathVariable("post_id") Long postId,
            @RequestParam(value = "page", defaultValue = "1") int page) {
        System.out.println("check");
        try{
            // 포스트 조회 서비스 호출
            PostDetailDTO postDetailDTO = postService.checkByPostId(postId);

            if(token!=null){
                boolean heart_state = heartService.findByPostAndMember(postId, tokenProvider.extractIdByAccessToken(token));
                postDetailDTO.setHeart(heart_state);
            }

            // PostResponse 반환
            return ResponseEntity.ok(postDetailDTO);
        }catch (Exception e){
            ResponseDTO responseDTO = ResponseDTO.builder()
                    .error(e.getMessage()).build();
            return ResponseEntity
                    .badRequest()
                    .body(responseDTO);
        }
    }

    // 게시글 저장
    @PostMapping("/post/write")
    public ResponseEntity<?> savePost( @RequestHeader("Authorization") String token,
                                       @RequestBody final PostRequest params) {

        try{
            PostSaveDTO postSaveDTO = postService.savePost(params,token);

            return ResponseEntity.ok(postSaveDTO);
        }catch (Exception e){
            ResponseDTO responseDTO = ResponseDTO.builder()
                    .error( e.getMessage()).build();
            return ResponseEntity
                    .badRequest()
                    .body(responseDTO);
        }
    }


    // 게시글 작성자 확인
    @GetMapping("/{category}/{post_id}/edit")
    public ResponseEntity<?> postEdit( @RequestHeader(value = "Authorization",required = false) String token,
                                    @PathVariable("category") String category,
                                    @PathVariable("post_id") Long postId
                                    ) {
        try{
            boolean writer_check = postService.checkPostWriter(token ,postId);

            return ResponseEntity.ok(writer_check);
        }catch (Exception e){
            ResponseDTO responseDTO = ResponseDTO.builder()
                    .error("checking fail").build();
            return ResponseEntity
                    .badRequest()
                    .body(responseDTO);
        }
    }

    //게시글 수정
    @PutMapping("/{category}/{post_id}/edit")
    public ResponseEntity<?> postEditSave( @RequestHeader("Authorization") String token,
                                           @PathVariable("category") String category,
                                           @PathVariable("post_id") Long postId,
                                           @RequestBody final PostRequest params
    ) {
        try{
            PostSaveDTO postSaveDTO = postService.updatePost(token ,postId, params);

            return ResponseEntity.ok(postSaveDTO);
        }catch(Exception e){
            ResponseDTO responseDTO = ResponseDTO.builder()
                    .error(e.getMessage()).build();
            return ResponseEntity
                    .badRequest()
                    .body(responseDTO);
        }
    }

    //게시글 삭제
    @DeleteMapping("/{category}/{post_id}/delete")
    public ResponseEntity<?> postDelete(
                                        @RequestHeader("Authorization") String token,
                                        @PathVariable("category") String category,
                                        @PathVariable("post_id") Long postId){
        ResponseDTO responseDTO;
        try{
            postService.deletePost(token,postId);
            responseDTO = ResponseDTO.builder().
                    message("delete success")
                    .build();
            return ResponseEntity.ok().body(responseDTO);
        }catch(Exception e){
            responseDTO = ResponseDTO.builder()
                    .error("editing fail").build();
            return ResponseEntity
                    .badRequest()
                    .body(responseDTO);
        }
    }



}

