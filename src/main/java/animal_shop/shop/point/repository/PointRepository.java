package animal_shop.shop.point.repository;

import animal_shop.shop.point.entity.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PointRepository extends JpaRepository<Point, Long> {


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
            "WHERE FUNCTION('YEAR', p.getDate) = :year " +
            "AND FUNCTION('MONTH', p.getDate) = :month " +
            "GROUP BY FUNCTION('DATE_FORMAT', p.getDate, '%Y-%m-%d'), p.sellerId " +
            "ORDER BY day, p.sellerId")
    List<Object[]> findDailyTotalPointsBySellerForMonth(@Param("year") int year, @Param("month") int month);



}
