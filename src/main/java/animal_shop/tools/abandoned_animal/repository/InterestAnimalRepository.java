package animal_shop.tools.abandoned_animal.repository;

import animal_shop.tools.abandoned_animal.entity.InterestAnimal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InterestAnimalRepository extends JpaRepository<InterestAnimal,Long> {

    //관심 동물 레포지토리
}
