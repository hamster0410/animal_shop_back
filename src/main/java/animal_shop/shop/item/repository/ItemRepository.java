package animal_shop.shop.item.repository;

import animal_shop.shop.item.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item,Long> {
    Page<Item> findBySpecies(Pageable pageable, String species);
    Page<Item> findAllByOrderByCreatedDateDesc(Pageable pageable);


}
