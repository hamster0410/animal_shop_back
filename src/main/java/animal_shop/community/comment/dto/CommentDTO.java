package animal_shop.community.comment.dto;

import animal_shop.community.comment.entity.Comment;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class CommentDTO {

    private Long id;
    private String content; // 댓글 내용
    private String nickname; // 댓글을 단 사용자
    private CommentDTO parent; // 대댓글의 부모 댓글을 DTO로 표현
    private LocalDateTime createdDate;
    private List<String> imageUrl;
    private Long countHeart = 0L; // 기본값으로 0으로 초기화
    private boolean heart;
    private Long postId;
    private String postCategory;


    // 기본 생성자 추가
    public CommentDTO() {}

    public CommentDTO(Comment comment) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.nickname = comment.getMember().getNickname();
        this.createdDate = comment.getCreatedDate();
        this.parent = (comment.getParent() != null) ? new CommentDTO(comment.getParent()) : null;
        this.imageUrl  = comment.getImageUrl();
        this.countHeart = comment.getCountHeart();
        this.postId = comment.getPostId();
        this.postCategory = comment.getPostCategory();
    }
}
