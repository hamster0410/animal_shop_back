package animal_shop.shop.item_comment.repository;

import animal_shop.shop.item.entity.Item;
import animal_shop.shop.item_comment.entity.ItemComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemCommentRepository extends JpaRepository<ItemComment,Long> {
    Page<ItemComment> findByItem(Item item, Pageable pageable);

}
