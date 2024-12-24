package animal_shop.global.pay.repository;

import animal_shop.global.pay.entity.KakaoCancelPay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KakaoCancelPayRepository extends JpaRepository<KakaoCancelPay,Long> {
}
