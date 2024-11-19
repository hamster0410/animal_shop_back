package animal_shop.shop.item.repository;

import animal_shop.shop.item.entity.Option;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OptionRepository extends JpaRepository<Option,Long> {
}
