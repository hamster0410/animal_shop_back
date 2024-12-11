package animal_shop.tools.map_service.repository;

import animal_shop.tools.map_service.entity.MapComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MapCommentRespository extends JpaRepository<MapComment,Long> {
    Page<MapComment> findByMapId(long mapId, Pageable pageable);

}
