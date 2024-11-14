package animal_shop.domain.heart_post.repository;

import animal_shop.domain.heart_post.entity.Heart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface HeartRepository extends JpaRepository<Heart, Long> {

    @Query("SELECT h FROM Heart h WHERE h.member.id = :memberId AND h.post.id = :postId")
    Heart findByMemberIdAndPostId( @Param("postId") Long postId,@Param("memberId") Long memberId);

}
