package animal_shop.shop.point.repository;

import animal_shop.shop.point.entity.PointWithdraw;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PointWithdrawRepository extends JpaRepository<PointWithdraw,Long> {
}
