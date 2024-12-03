package animal_shop.global.admin.repository;

import animal_shop.global.admin.entity.StopItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StopItemRepository extends JpaRepository<StopItem, Long> {
}
