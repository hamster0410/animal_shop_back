package animal_shop.tools.map_service.repository;

import animal_shop.tools.map_service.entity.MapEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MapRepository extends JpaRepository<MapEntity,Long> , JpaSpecificationExecutor<MapEntity> {


    @Query("SELECT m " +
            "FROM MapEntity m " +
            "WHERE m.category3 = :category3 " +
            "AND m.facilityName = :facilityName " +
            "AND m.lotAddress = :lotAddress")
    Optional<MapEntity> findUniqueByCategory3AndFacilityNameAndLotAddress(
            @Param("category3") String category3,
            @Param("facilityName") String facilityName,
            @Param("lotAddress") String lotAddress);

}
