package animal_shop.tools.map_service.entity;

import animal_shop.community.member.entity.Member;
import jakarta.persistence.*;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;



@Entity
@Table(name = "map_Like")
@NoArgsConstructor
@Getter
public class MapLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "map_id", nullable = false)
    private MapEntity map; // MapEntity 타입으로 변경

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member; // 좋아요를 누른 사용자

    @Builder
    public MapLike(MapEntity map, Member member) {
        this.map = map;
        this.member = member;
    }
}
