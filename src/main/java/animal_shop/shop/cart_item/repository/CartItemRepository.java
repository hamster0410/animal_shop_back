//package animal_shop.shop.cart_item.repository;
//
//import animal_shop.shop.cart_item.entity.CartItem;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.stereotype.Repository;
//
//@Repository
//public interface CartItemRepository extends JpaRepository<CartItem, Long> {
//
//    CartItem findByCartIdAndItemId(Long cartId, Long itemId);
//
//    @Query("select new animal_shop.shop.cart.dto.CartDetailDTO(ci.id, i.itemNm, i.price, ci.count) " +
//            "from CartItem ci join ci.item i " +
//            "where ci.cart.id = :cartId " +
//            "order by ci.")
//
//}
