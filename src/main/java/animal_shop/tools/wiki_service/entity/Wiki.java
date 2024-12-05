package animal_shop.tools.wiki_service.entity;

import animal_shop.global.dto.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "wiki")
@NoArgsConstructor
@AllArgsConstructor
@Setter @Getter
public class Wiki extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wiki_id")
    private Long id;


    @Column(name = "breed_id")
    private Long breedId;        // 품종 ID

    private String contents;

    @Column(name = "attachment_url", length = 255)
    private String attachmentUrl;   // 첨부파일 URL

    @Column(name = "breed_name", nullable = false, length = 100)
    private String breedName;       // 품종 이름

    @Column(name = "overview", length = 65535)
    private String overview;        // 개요

    @Column(name = "appearance", length = 65535)
    private String appearance;      // 외모

    @Column(name = "temperament", length = 65535)
    private String temperament;     // 성격

    @OneToMany(mappedBy = "wiki", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WikiComment> comments;
}