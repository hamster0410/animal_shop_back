package animal_shop.tools.wiki_service.repository;

import animal_shop.tools.wiki_service.entity.Wiki;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import java.awt.print.Pageable;

public interface WikiRepository extends JpaRepository<Wiki,Long> {
    static Page<Wiki> findAll(Pageable pageable) {
        return null;
    }
}
