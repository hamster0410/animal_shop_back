package animal_shop.shop.order_item.repository;

import animal_shop.shop.order_item.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    @Query("SELECT MIN(o.createdDate) FROM OrderItem o")
    LocalDateTime findEarliestOrderItemDate();

    //판매자 시간대별 아이템 합계
    @Query(value =
            "SELECT " +
                    "CASE " +
                    "    WHEN :time = 'day' THEN DATE_FORMAT(oi.created_date, '%Y-%m-%d') " +
                    "    WHEN :time = 'month' THEN DATE_FORMAT(oi.created_date, '%Y-%m') " +
                    "    WHEN :time = 'year' THEN DATE_FORMAT(oi.created_date, '%Y') " +
                    "    ELSE DATE_FORMAT(oi.created_date, '%Y') " + // 기본값: 년 단위
                    "END AS groupDate, " +
                    "COUNT(*) AS itemCount " +
                    "FROM order_item oi " +
                    "JOIN item i ON oi.item_id = i.item_id " +
                    "WHERE i.member_id = :memberId " +
                    "AND (:start IS NULL OR oi.created_date >= :start) " + // 시작일 조건 추가
                    "AND (:end IS NULL OR oi.created_date <= :end) " +   // 종료일 조건 추가
                    "GROUP BY groupDate " +
                    "ORDER BY groupDate DESC",
            nativeQuery = true)
    List<Object[]> totalItemTime(
            @Param("memberId") Long memberId,
            @Param("time") String time,
            @Param("start") String start,
            @Param("end") String end);

    //판매자 총 아이템 합계
    @Query(value =
            "SELECT " +
                    "'all time' AS customLabel, " + // 문자열은 싱글 쿼테이션으로 감싸야 함
                    "COUNT(*) AS itemCount " +
                    "FROM order_item oi " +
                    "JOIN item i ON oi.item_id = i.item_id " +
                    "WHERE i.member_id = :memberId " +
                    "AND (:start IS NULL OR oi.created_date >= :start) " + // 시작일 조건 추가
                    "AND (:end IS NULL OR oi.created_date <= :end) ",      // 종료일 조건 추가
            nativeQuery = true)
    List<Object[]> totalItem(
            @Param("memberId") Long memberId,
            @Param("start") String start,
            @Param("end") String end);
}
