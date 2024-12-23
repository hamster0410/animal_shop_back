package animal_shop.community.comment.service;

import animal_shop.community.comment.dto.CommentDTO;
import animal_shop.community.comment.dto.CommentResponseDTO;
import animal_shop.community.comment.dto.RequestCommentDTO;
import animal_shop.community.comment.entity.Comment;
import animal_shop.community.comment.repository.CommentRepository;
import animal_shop.community.heart_comment.entity.CommentHeart;
import animal_shop.community.heart_comment.repository.CommentHeartRepository;
import animal_shop.community.member.entity.Member;
import animal_shop.community.member.service.MemberService;
import animal_shop.community.post.entity.Post;
import animal_shop.community.post.service.PostService;
import animal_shop.global.security.TokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Slf4j
@Service
public class CommentService {
    @Value("${file.upload-dir-comment}")  // 파일 저장 경로를 application.properties에 설정
    private String uploadDir;


    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private CommentHeartRepository commentHeartRepository;

    @Autowired
    private MemberService memberService;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private PostService postService;

    @Transactional
    public Comment getByCommentId(Long commentId) {

        return commentRepository.findById(commentId).orElseThrow();
    }
    @Transactional
    public CommentResponseDTO getCommentsByPostId(Long postId,String token) {

        List<CommentDTO> commentDTOS = new ArrayList<>();
        List<Comment> comments = commentRepository.findByPost_Id(postId);

        if(token!=null){
            String userId = tokenProvider.extractIdByAccessToken(token);
            for(Comment comment : comments){
                CommentDTO commentDTO = new CommentDTO(comment);
                CommentHeart commentheart = commentHeartRepository.findByMemberIdAndCommentId(comment.getId(), Long.valueOf(userId));
                commentDTO.setHeart(commentheart!=null);
                commentDTOS.add(commentDTO);
            }
        }else{
            commentDTOS = comments.stream()
                    .map(CommentDTO::new)  // Comment 객체를 CommentDTO로 변환
                    .toList();

        }


        return CommentResponseDTO
                .builder()
                .comments(commentDTOS)
                .totalCommentCount(commentDTOS.size())
                .build();

    }

    @Transactional
    public void createComment(String token, Long postId, RequestCommentDTO requestCommentDTO, List<MultipartFile> imageFiles) throws IOException {

        String userId = tokenProvider.extractIdByAccessToken(token);
        Member member = memberService.getByUserId(Long.valueOf(userId));
        Post post = postService.getByPostId(postId);

        if (imageFiles == null) {
            imageFiles = new ArrayList<>();
        }
        //image 저장
        List<String> imageUrl = null;
        if(!imageFiles.isEmpty()) imageUrl = saveImage(imageFiles,userId);

        log.info("here1 ");
        Comment parent_comment;
        if(requestCommentDTO.getParentId() != null){
            parent_comment = commentRepository.findById(requestCommentDTO.getParentId()).get();
        }else{
            parent_comment = null;
        }

        log.info("here2");
        Comment comment = Comment.builder()
                .imageUrl(imageUrl)
                .post(post)
                .content(requestCommentDTO.getContent())
                .parent(parent_comment)
                .countHeart(0L)
                .member(member)
                .build();

        postService.increaseComment(post);
        commentRepository.save(comment);
    }

    @Transactional
    public CommentDTO updateComment(String token, Long commentId, RequestCommentDTO commentDTO, List<MultipartFile> imageFiles) throws IOException {
        String userId = tokenProvider.extractIdByAccessToken(token);
        Long UID = Long.valueOf(userId);
        Optional<Comment> comment = commentRepository.findById(commentId);

        if (imageFiles == null) {
            imageFiles = new ArrayList<>();
        }
        //image 저장
        List<String> imageUrl = null;
        if(!imageFiles.isEmpty()) imageUrl = saveImage(imageFiles,userId);

        //코멘트가 존재하고 사용자가 쓴 글이 맞으면
        if(comment.isPresent() && UID.equals(comment.get().getMember().getId())){

            comment.get().setContent(commentDTO.getContent());
            comment.get().setImageUrl(imageUrl);
            commentRepository.save(comment.get());

            return new CommentDTO(comment.get());
        }else{
            throw new IllegalArgumentException("comment is not present");
        }
    }
    @Transactional
    public void deleteComment(String token, Long commentId) {
        Long userId = Long.valueOf(tokenProvider.extractIdByAccessToken(token));
        Optional<Comment> comment = commentRepository.findById(commentId);

        Post post = comment.get().getPost();
        postService.decreaseComment(post);

        if(comment.isPresent() && userId.equals(comment.get().getMember().getId())){
            commentRepository.delete(comment.get());
        }else{
            throw new IllegalArgumentException("comment is not present");
        }

    }

    public List<String> saveImage(List<MultipartFile> imageFiles,String userId) throws IOException {
        List<String> returnFiles = new ArrayList<>();
        Date date = new Date();
        for(MultipartFile imageFile : imageFiles){
            if (imageFile != null && !imageFile.isEmpty()) {
                // 고유한 파일 이름 생성
                String uniqueFileName =  userId + "_" + date.getDate() + "_" + imageFile.getOriginalFilename();
                File destinationFile = new File(uploadDir + File.separator + uniqueFileName);

                // 파일 시스템에 이미지 파일 저장
                imageFile.transferTo(destinationFile);

                // 이미지 URL 설정 (예: http://localhost:8080/images/파일명)
                returnFiles.add(uniqueFileName);
            }
        }
        return  returnFiles;
    }
    @Transactional
    public boolean checkCommentWriter(String token, Long commentId) {
        String userId = tokenProvider.extractIdByAccessToken(token);
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new IllegalArgumentException("comment not found : " + commentId));
        if(String.valueOf(comment.getMember().getId()).equals(userId)){
            return true;
        }else{
            return false;
        }
    }

    @Transactional
    public void increaseHeart(Comment comment) {
        comment.setCountHeart(comment.getCountHeart() + 1);
        commentRepository.save(comment);
    }

    @Transactional
    public void decreaseHeart(Comment comment) {
        comment.setCountHeart(comment.getCountHeart() - 1);
        commentRepository.save(comment);
    }
}
