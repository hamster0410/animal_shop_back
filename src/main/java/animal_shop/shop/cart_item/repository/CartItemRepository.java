package animal_shop.shop.cart_item.repository;

import animal_shop.shop.cart_item.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    CartItem findByCartIdAndItemIdAndOptionId(Long cartId, Long itemId,Long optionId);


    List<CartItem> findByCartIdOrderByCreatedDateDesc(Long cartId);


}
