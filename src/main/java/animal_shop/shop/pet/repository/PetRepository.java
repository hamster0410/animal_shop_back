package animal_shop.shop.pet.repository;

import animal_shop.community.member.entity.Member;
import animal_shop.shop.pet.entity.Pet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {
    Page<Pet> findByMember(Member member, Pageable pageable);
    List<Pet> findByMember(Member member);


}


