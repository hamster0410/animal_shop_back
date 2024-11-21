package animal_shop.shop.cart_item.repository;

import animal_shop.shop.cart_item.entity.CartItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    CartItem findByCartIdAndItemIdAndOptionId(Long cartId, Long itemId,Long optionId);


    Page<CartItem> findByCartIdOrderByCreatedDateDesc(Long cartId, Pageable pageable);


}
