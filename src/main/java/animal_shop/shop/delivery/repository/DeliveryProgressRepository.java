package animal_shop.shop.delivery.repository;

import animal_shop.shop.delivery.DeliveryStatus;
import animal_shop.shop.delivery.entity.DeliveryProgress;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DeliveryProgressRepository extends JpaRepository<DeliveryProgress, Long> {
    List<DeliveryProgress> findAllByDeliveryStatusAndDeliveredDateBefore(DeliveryStatus deliveryStatus, LocalDateTime localDateTime);

    Page<DeliveryProgress> findByBuyerId(Long BuyerId, Pageable pageable);
}
