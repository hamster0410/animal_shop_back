package animal_shop.tools.map_service.entity;

import animal_shop.community.member.entity.Member;
import animal_shop.global.dto.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class MapComment extends BaseTimeEntity  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "map_comment_id")
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id") // FK 설정
    private Member member;

    private String contents;

    private long mapId;

    @ElementCollection
    @CollectionTable(name = "map_comment_thumbnail_urls", joinColumns = @JoinColumn(name = "map_comment_id"))
    @Column(name = "map_comment_thumbnail_url")
    private List<String> map_comment_thumbnail_url;

    private Long rating;

}
