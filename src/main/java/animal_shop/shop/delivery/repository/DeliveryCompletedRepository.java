package animal_shop.shop.delivery.repository;

import animal_shop.shop.delivery.entity.DeliveryCompleted;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryCompletedRepository extends JpaRepository<DeliveryCompleted, Long> {
    Page<DeliveryCompleted> findByBuyerId(Long BuyerId, Pageable pageable);
}
