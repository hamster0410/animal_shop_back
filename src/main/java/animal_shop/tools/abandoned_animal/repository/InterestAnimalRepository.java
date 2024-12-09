package animal_shop.tools.abandoned_animal.repository;

import animal_shop.community.member.entity.Member;
import animal_shop.tools.abandoned_animal.entity.InterestAnimal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InterestAnimalRepository extends JpaRepository<InterestAnimal,Long> {
    Page<InterestAnimal> findByMember(Member member, Pageable pageable);

    //관심 동물 레포지토리
}
