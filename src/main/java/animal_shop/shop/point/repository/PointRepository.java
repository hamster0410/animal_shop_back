package animal_shop.shop.point.repository;

import animal_shop.shop.point.entity.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PointRepository extends JpaRepository<Point, Long> {

    // 가장 빠른 포인트 일자를 가져오는 쿼리
    @Query("SELECT MIN(p.getDate) FROM Point p")
    LocalDateTime findEarliestPointDate();

    //월별 전체 합계
    @Query("SELECT FUNCTION('DATE_FORMAT', p.getDate, '%Y-%m') AS month, SUM(p.point) AS totalPoints " +
            "FROM Point p " +
            "WHERE FUNCTION('YEAR', p.getDate) = :year " +
            "GROUP BY FUNCTION('DATE_FORMAT', p.getDate, '%Y-%m') " +
            "ORDER BY month")
    List<Object[]> findMonthlyPointSumsByYear(@Param("year") int year);

    //일별 전체 합계
    @Query("SELECT FUNCTION('DATE_FORMAT', p.getDate, '%Y-%m-%d') AS day, SUM(p.point) AS totalPoints " +
            "FROM Point p " +
            "WHERE FUNCTION('YEAR', p.getDate) = :year " +
            "AND FUNCTION('MONTH', p.getDate) = :month " +
            "GROUP BY FUNCTION('DATE_FORMAT', p.getDate, '%Y-%m-%d') " +
            "ORDER BY day")
    List<Object[]> findDailyPointSumsByYearAndMonth(@Param("year") int year, @Param("month") int month);

    //연별 판매자별 합계
    @Query("SELECT p.sellerId, SUM(p.point) AS totalPoints " +
            "FROM Point p " +
            "WHERE FUNCTION('YEAR', p.getDate) = :year " +
            "GROUP BY p.sellerId " +
            "ORDER BY p.sellerId")
    List<Object[]> findTotalPointsBySellerAndYear(@Param("year") int year);

    //월별 판매자별 합계
    @Query("SELECT p.sellerId, SUM(p.point) AS totalPoints " +
            "FROM Point p " +
            "WHERE FUNCTION('YEAR', p.getDate) = :year " +
            "AND FUNCTION('MONTH', p.getDate) = :month " +
            "GROUP BY p.sellerId " +
            "ORDER BY p.sellerId")
    List<Object[]> findTotalPointsBySellerAndMonth(@Param("year") int year, @Param("month") int month);

    //일별 판매자별 합계
    @Query("SELECT FUNCTION('DATE_FORMAT', p.getDate, '%Y-%m-%d') AS day, p.sellerId, SUM(p.point) AS totalPoints " +
            "FROM Point p " +
            "WHERE (:year IS NULL OR FUNCTION('YEAR', p.getDate) = :year ) " +
            "AND (:month IS NULL OR FUNCTION('MONTH', p.getDate) = :month ) " +
            "AND (:day IS NULL OR FUNCTION('DAY', p.getDate) = :day ) " +
            "GROUP BY FUNCTION('DATE_FORMAT', p.getDate, '%Y-%m-%d'), p.sellerId " +
            "ORDER BY day, p.sellerId")
    List<Object[]> findDailyTotalPointsBySellerForDay(@Param("year") int year, @Param("month") int month, @Param("day") int day);



    //판매자 시간대별 수익 합계
    @Query(value =
            "SELECT " +
                    "CASE " +
                    "    WHEN :day IS NOT NULL THEN DATE_FORMAT(p.get_date, '%Y-%m-%d') " +
                    "    WHEN :month IS NOT NULL THEN DATE_FORMAT(p.get_date, '%Y-%m') " +
                    "    ELSE DATE_FORMAT(p.get_date, '%Y') " +
                    "END AS groupDate, " +
                    "p.item_name AS item_name, " +
                    "p.option_name AS option_name, " +
                    "SUM(p.point) AS totalPoints " +
                    "FROM Point p " +
                    "WHERE (:year IS NULL OR YEAR(p.get_date) = :year) " +
                    "AND (:month IS NULL OR MONTH(p.get_date) = :month) " +
                    "AND (:day IS NULL OR DAY(p.get_date) = :day) " +
                    "AND p.seller_id = :memberId " +
                    "GROUP BY groupDate, p.item_name, p.option_name " +
                    "ORDER BY groupDate, p.item_name",
            nativeQuery = true)
    List<Object[]> findTotalPointsByItemIds(
            @Param("memberId") Long memberId,
            @Param("year") Integer year,
            @Param("month") Integer month,
            @Param("day") Integer day);


    @Query(value =
            "SELECT " +
                    "CASE " +
                    "    WHEN :day IS NOT NULL THEN DATE_FORMAT(oi.created_date, '%Y-%m-%d') " +
                    "    WHEN :month IS NOT NULL THEN DATE_FORMAT(oi.created_date, '%Y-%m') " +
                    "    ELSE DATE_FORMAT(oi.created_date, '%Y') " +
                    "END AS groupDate, " +
                    "oi.order_name AS order_name, " +
                    "i.name AS name, " +
                    "COUNT(*) AS totalPoints " +

                    "FROM order_item oi " +
                    "JOIN item i ON oi.item_id = i.item_id " +
                    "WHERE (:year IS NULL OR YEAR(oi.created_date) = :year) " +
                    "AND (:month IS NULL OR MONTH(oi.created_date) = :month) " +
                    "AND (:day IS NULL OR DAY(oi.created_date) = :day) " +
                    "AND i.member_id = :memberId " +
                    "GROUP BY groupDate, oi.order_name, i.item_id " +
                    "ORDER BY groupDate, oi.order_name",
            nativeQuery = true)
    List<Object[]> findTotalItemByOrderItem(
            @Param("memberId") Long memberId,
            @Param("year") Integer year,
            @Param("month") Integer month,
            @Param("day") Integer day);



}
