package animal_shop.shop.item_comment.repository;

import animal_shop.shop.item.entity.Item;
import animal_shop.shop.item_comment.entity.ItemComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ItemCommentRepository extends JpaRepository<ItemComment,Long> {
    Page<ItemComment> findByItemId(Long itemId, Pageable pageable);

    Page<ItemComment> findByMemberId(Long userId, Pageable pageable);

    // 사진 수가 많은 순

    @Query(value = "SELECT ic.* FROM item_comment ic " +
            "LEFT JOIN item_comment_thumbnail_urls urls ON ic.item_comment_id = urls.item_comment_id " +
            "WHERE ic.item_id = :item_id " +
            "GROUP BY ic.item_comment_id " +
            "ORDER BY COUNT(urls.comment_thumbnail_url) DESC, ic.rating DESC", nativeQuery = true)
    Page<ItemComment> findByMostPhotosAndRatingNative(@Param("item_id") Long itemId, Pageable pageable);


    // countHeart가 많은 순
    @Query(value = "SELECT * FROM item_comment " +
            "WHERE item_id = :item_id " +
            "ORDER BY count_heart DESC", nativeQuery = true)
    Page<ItemComment> findByMostHeartsNative(Long item_id,Pageable pageable);

    // rating이 높은 순
    @Query(value = "SELECT * FROM item_comment " +
            "WHERE item_id = :item_id " +
            "ORDER BY rating DESC", nativeQuery = true)
    Page<ItemComment> findByHighestRatingNative(Long item_id,Pageable pageable);

}
