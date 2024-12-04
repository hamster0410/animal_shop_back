package animal_shop.shop.cart_item.repository;

import animal_shop.shop.cart_item.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long>{

    CartItem findByCartIdAndItemIdAndOptionId(Long cartId, Long itemId,Long optionId);


    List<CartItem> findByCartIdOrderByCreatedDateDesc(Long cartId);

//    @Query("SELECT ci.item, COUNT(ci) " +
//            "FROM CartItem ci " +
//            "JOIN ci.cart c " +
//            "WHERE c.member = :member " +
//            "AND (:year IS NULL OR YEAR(ci.createdDate) = :year) " +
//            "AND (:month IS NULL OR MONTH(ci.createdDate) = :month) " +
//            "AND ci.item IN :items " +
//            "GROUP BY ci.item")
//    List<Object[]> countCartItemsByItemAndMemberAndDate(@Param("member") Member member,
//                                                        @Param("year") Integer year,
//                                                        @Param("month") Integer month,
//                                                        @Param("items") List<Item> items);



}
