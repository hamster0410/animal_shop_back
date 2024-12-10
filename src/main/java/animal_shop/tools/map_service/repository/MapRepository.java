package animal_shop.tools.map_service.repository;

import animal_shop.tools.map_service.entity.MapEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface MapRepository extends JpaRepository<MapEntity,Long> , JpaSpecificationExecutor<MapEntity> {


}
