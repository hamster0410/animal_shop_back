package animal_shop.shop.cart_item.repository;

import animal_shop.community.member.entity.Member;
import animal_shop.shop.cart_item.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long>{

    CartItem findByCartIdAndItemIdAndOptionId(Long cartId, Long itemId,Long optionId);


    List<CartItem> findByCartIdOrderByCreatedDateDesc(Long cartId);

    @Query("SELECT i.name, SUM(ci.count) " +
            "FROM CartItem ci " +
            "JOIN ci.item i " +
            "WHERE i.member = :member " +
            "AND (:year IS NULL OR FUNCTION('YEAR', ci.createdDate) = :year) " +
            "AND (:month IS NULL OR FUNCTION('MONTH', ci.createdDate) = :month) " +
            "GROUP BY i.name")
    List<Object[]> countItemsByMemberAndDate(@Param("member") Member member,
                                             @Param("year") Integer year,
                                             @Param("month") Integer month);

}
