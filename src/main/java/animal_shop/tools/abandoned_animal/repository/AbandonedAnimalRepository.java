package animal_shop.tools.abandoned_animal.repository;

import animal_shop.tools.abandoned_animal.entity.AbandonedAnimal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface AbandonedAnimalRepository extends JpaRepository<AbandonedAnimal, Long> , JpaSpecificationExecutor<AbandonedAnimal> {
    boolean existsByDesertionNo(String desertionNo);
}
