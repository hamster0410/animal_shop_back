package animal_shop.tools.map_service.repository;

import animal_shop.community.member.entity.Member;
import animal_shop.tools.map_service.entity.MapComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MapCommentRespository extends JpaRepository<MapComment,Long> {
    Page<MapComment> findByMapId(long mapId, Pageable pageable);

    Page<MapComment> findByMember(Member member, Pageable pageable);
}
