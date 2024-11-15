package animal_shop.community.heart_comment.repository;

import animal_shop.community.heart_comment.entity.CommentHeart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentHeartRepository extends JpaRepository<CommentHeart, Long> {

    @Query("SELECT h FROM CommentHeart h WHERE h.member.id = :memberId AND h.comment.id = :commentId")
    CommentHeart findByMemberIdAndCommentId(@Param("commentId") Long commentId, @Param("memberId") Long memberId);

}
