package animal_shop.tools.Like.entity;

import animal_shop.community.member.entity.Member;
import animal_shop.tools.map_service.entity.MapEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.mapping.Map;

@Table(name = "LIKE")
@Entity
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name ="mapId",nullable = false)
    private MapEntity map;


    @ManyToOne
    @JoinColumn(name = "member_id",nullable = false)
    private Member member;

    public Like(Member member, MapEntity map){
        this.map = map;
        this.member = member;
    }
}
