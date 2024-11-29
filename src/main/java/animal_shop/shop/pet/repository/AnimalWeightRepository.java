package animal_shop.shop.pet.repository;

import animal_shop.shop.pet.entity.AnimalWeight;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnimalWeightRepository extends JpaRepository<AnimalWeight, Long> {
    AnimalWeight findBySpecies(String species);
}
