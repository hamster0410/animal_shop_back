package animal_shop.shop.pet.repository;

import animal_shop.shop.pet.entity.AnimalWeight;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnimalWeightRepository extends JpaRepository<AnimalWeight, Long> {
    List<AnimalWeight> findBySpecies(String species);

    AnimalWeight findByBreed(String breed);
}
