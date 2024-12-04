package animal_shop.community.post.entity;

import animal_shop.global.dto.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "dog_breed")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Wiki extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "breed_id")
    private Long breedId;        // 품종 ID

    @Column(name = "attachment_url", nullable = false, length = 255)
    private String attachmentUrl;   // 첨부파일 URL

    @Column(name = "breed_name", nullable = false, length = 100)
    private String breedName;       // 품종 이름

    @Column(name = "overview", length = 65535)
    private String overview;        // 개요

    @Column(name = "appearance", length = 65535)
    private String appearance;      // 외모

    @Column(name = "temperament", length = 65535)
    private String temperament;     // 성격

}
