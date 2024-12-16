package animal_shop.tools.wiki_service.repository;

import animal_shop.tools.wiki_service.entity.WikiComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface WikiCommentRepository extends JpaRepository<WikiComment,Long> {

    Page<WikiComment> findByWikiId(Long wikiId, Pageable pageable);
}
