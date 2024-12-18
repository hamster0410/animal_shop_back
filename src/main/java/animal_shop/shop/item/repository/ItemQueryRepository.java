package animal_shop.shop.item.repository;

import animal_shop.shop.item.entity.ItemQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ItemQueryRepository extends JpaRepository<ItemQuery,Long> {

    Page<ItemQuery> findOrdersBySellerId(Long sellerId, Pageable pageable);

    Page<ItemQuery> findByItemId(Long itemId, Pageable pageable);

    Page<ItemQuery> findByCustomerId(Long memberId, Pageable pageable);

}
