package animal_shop.community.comment.repository;

import animal_shop.community.comment.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    Long countByPostId(Long postId);

    List<Comment> findByPostId(Long postId);

    Page<Comment> findByMemberId(Long memberId, Pageable pageable);


}
