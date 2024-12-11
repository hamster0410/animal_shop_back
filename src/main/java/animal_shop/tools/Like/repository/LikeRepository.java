package animal_shop.tools.Like.repository;

import animal_shop.tools.Like.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends JpaRepository<Like,Long> {

    @Query("SELECT l FROM Like l WHERE l.member.id = :memberId AND l.map.id = :mapId")
    Like findByMemberIdAndMapId(@Param("mapId") Long mapId, @Param("memberId") Long memberId);

}