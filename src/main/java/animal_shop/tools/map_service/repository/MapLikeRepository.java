package animal_shop.tools.map_service.repository;

import animal_shop.tools.map_service.entity.MapLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MapLikeRepository extends JpaRepository<MapLike,Long> {
    @Query("SELECT m FROM MapLike m WHERE m.member.id = :memberId AND m.map.id = :mapId")
    MapLike findByMemberIdAndMapId(@Param("mapId") Long mapId, @Param("memberId") Long memberId);
}