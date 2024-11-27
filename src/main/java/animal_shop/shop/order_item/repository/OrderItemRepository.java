package animal_shop.shop.order_item.repository;

import animal_shop.shop.order_item.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
