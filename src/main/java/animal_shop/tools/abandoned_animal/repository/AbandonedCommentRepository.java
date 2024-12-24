package animal_shop.tools.abandoned_animal.repository;


import animal_shop.tools.abandoned_animal.entity.AbandonedAnimal;
import animal_shop.tools.abandoned_animal.entity.AbandonedComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AbandonedCommentRepository extends JpaRepository<AbandonedComment, Long> {

    Page<AbandonedComment> findByAbandonedAnimal(AbandonedAnimal abandonedAnimal, Pageable pageable);

    Page<AbandonedComment> findByUserId(Long userId, Pageable pageable);

}
