package animal_shop.shop.order.repository;

import animal_shop.shop.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {

    @Query("select o from Order o " +
            "where o.member.id = :member_id " +
            "order by o.orderDate desc"
    )
    List<Order> findOrders(@Param("member_id") String member_id);

    Order findByOrderCode(String OrderCode);
}
