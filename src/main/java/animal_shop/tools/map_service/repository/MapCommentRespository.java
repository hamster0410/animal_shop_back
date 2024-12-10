package animal_shop.tools.map_service.repository;

import animal_shop.tools.map_service.entity.MapComment;
import animal_shop.tools.map_service.entity.MapEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MapCommentRespository extends JpaRepository<MapComment,Long> {
}
