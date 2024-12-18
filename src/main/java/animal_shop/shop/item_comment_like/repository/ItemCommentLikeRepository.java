package animal_shop.shop.item_comment_like.repository;

import animal_shop.shop.item_comment_like.entity.ItemCommentLike;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemCommentLikeRepository extends JpaRepository<ItemCommentLike,Long> {


    @Query("SELECT icl FROM ItemCommentLike icl WHERE icl.itemComment.id = :itemCommentId AND icl.member.id = :memberId")
    ItemCommentLike findByItemCommentIdAndMemberId(@Param("itemCommentId") Long itemCommentId, @Param("memberId") Long memberId);


    Page<ItemCommentLike> findByMemberId(Long userId, Pageable pageable);
}
