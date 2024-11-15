package animal_shop.community.comment.repository;

import animal_shop.community.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Long countByPostId(Long postId);

    List<Comment> findByPostId(Long postId);
}
