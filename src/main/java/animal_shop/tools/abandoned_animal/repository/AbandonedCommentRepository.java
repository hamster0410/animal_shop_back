package animal_shop.tools.abandoned_animal.repository;


import animal_shop.tools.abandoned_animal.dto.AbandonedCommentDTO;
import animal_shop.tools.abandoned_animal.entity.AbandonedComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AbandonedCommentRepository extends JpaRepository<AbandonedComment, Long> {
    @Query("SELECT new animal_shop.tools.abandoned_animal.dto.AbandonedCommentDTO(" +
            "a.content, a.author, a.createdDate, a.lastModifiedDate) " +
            "FROM AbandonedComment a")
    Page<AbandonedCommentDTO> findAllComments(Pageable pageable);
}
