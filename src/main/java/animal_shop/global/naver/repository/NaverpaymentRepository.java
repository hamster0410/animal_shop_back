package animal_shop.global.naver.repository;


import animal_shop.global.naver.entity.Naverpayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface NaverpaymentRepository extends JpaRepository<Naverpayment,Long> {
    Optional<Naverpayment> findByMerchantPayKey(String paymentId);
}
