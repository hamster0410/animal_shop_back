package animal_shop.shop.delivery.repository;

import animal_shop.shop.delivery.entity.DeliveryItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryItemRepository extends JpaRepository<DeliveryItem, Long> {

}
