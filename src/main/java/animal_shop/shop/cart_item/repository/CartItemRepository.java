package animal_shop.shop.cart_item.repository;

import animal_shop.shop.cart.dto.CartDetailDTO;
import animal_shop.shop.cart_item.entity.CartItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    CartItem findByCartIdAndItemIdAndOptionId(Long cartId, Long itemId,Long optionId);

    @Query("select new animal_shop.shop.cart.dto.CartDetailDTO(ci.id, i.name, ci.count) " +
            "from CartItem ci join ci.item i " +
            "where ci.cart.id = :cartId " +
            "order by ci.createdDate desc")
    Page<CartDetailDTO> findCartDetailDtoList(@Param("cartId") Long cartId, Pageable pageable);



}
