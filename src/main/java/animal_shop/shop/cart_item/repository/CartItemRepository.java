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

    @Query(value =
            "SELECT " +
                    "CASE " +
                    "    WHEN :time = 'day' THEN DATE_FORMAT(ci.created_date, '%Y-%m-%d') " +
                    "    WHEN :time = 'month' THEN DATE_FORMAT(ci.created_date, '%Y-%m') " +
                    "    WHEN :time = 'year' THEN DATE_FORMAT(ci.created_date, '%Y') " +
                    "    ELSE DATE_FORMAT(ci.created_date, '%Y') " + // 기본값: 년 단위
                    "END AS groupDate, " +
                    "COUNT(*) AS totalCartItems " +
                    "FROM cart_item ci " +
                    "JOIN item i ON ci.item_id = i.item_id " +
                    "WHERE i.member_id = :userId " +
                    "AND (:start IS NULL OR ci.created_date >= :start) " + // 시작일 조건 추가
                    "AND (:end IS NULL OR ci.created_date <= :end) " +   // 종료일 조건 추가
                    "GROUP BY groupDate " +
                    "ORDER BY groupDate DESC",
            nativeQuery = true)
    List<Object[]> cartItemForTime(@Param("time")String time,
                                   @Param("userId")Long userId,
                                   @Param("start") String start,
                                   @Param("end")String end);

    @Query(value =
            "SELECT " +
                    "'ALL TIME', " +
                    "COUNT(*) AS totalCartItems " +
                    "FROM cart_item ci " +
                    "JOIN item i ON ci.item_id = i.item_id " +
                    "WHERE i.member_id = :userId ",
            nativeQuery = true)
    List<Object[]> cartItemForAll(
                                   @Param("userId")Long userId);

}
