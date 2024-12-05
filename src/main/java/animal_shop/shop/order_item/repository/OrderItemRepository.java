package animal_shop.shop.order_item.repository;

import animal_shop.shop.order_item.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    @Query("SELECT MIN(o.createdDate) FROM OrderItem o")
    LocalDateTime findEarliestOrderItemDate();
}
