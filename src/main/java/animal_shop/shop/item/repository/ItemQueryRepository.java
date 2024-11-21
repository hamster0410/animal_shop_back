package animal_shop.shop.item.repository;

import animal_shop.shop.item.entity.ItemQuery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.lang.reflect.Member;
import java.util.Optional;


public interface ItemQueryRepository extends JpaRepository<ItemQuery,Long> {

}
