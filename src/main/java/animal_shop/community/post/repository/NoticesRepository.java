package animal_shop.community.post.repository;

import animal_shop.community.post.entity.Notices;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoticesRepository extends  JpaRepository<Notices,Long> {
//    List<Notices> findByPriorityGreaterThan(Integer priority);
    Page<Notices> findAllByOrderByPriorityAsc(Pageable pageable);

}
