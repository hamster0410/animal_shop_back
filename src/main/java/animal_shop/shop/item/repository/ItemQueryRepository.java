package animal_shop.shop.item.repository;

import animal_shop.shop.item.entity.ItemQuery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemQueryRepository extends JpaRepository<ItemQuery,Long> {
}
