package animal_shop.shop.pet.repository;

import animal_shop.shop.pet.entity.PetEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PetRepository extends JpaRepository<PetEntity, Long> {



}
