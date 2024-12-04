package animal_shop.community.post.repository;

import animal_shop.community.post.entity.Wiki;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import java.awt.print.Pageable;

public interface WikiRepository extends JpaRepository<Wiki,Long> {
    static Page<Wiki> findAll(Pageable pageable) {
        return null;
    }
}
