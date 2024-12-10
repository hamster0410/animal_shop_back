package animal_shop.tools.map_service.entity;

import animal_shop.community.member.entity.Member;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class MapComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "map_comment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id") // FK 설정
    private Member member;

    private String contents;

    private long map_id;

    @ElementCollection
    @CollectionTable(name = "map_comment_thumbnail_urls", joinColumns = @JoinColumn(name = "map_comment_id"))
    @Column(name = "map_comment_thumbnail_url")
    private List<String> map_comment_thumbnail_url;

//    @OneToMany(mappedBy = "mapComment", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<ItemCommentLike> likes;
//
    private Long rating;
//
//    private Long countLike;
//
}
