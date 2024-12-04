package animal_shop.community.post.dto;


import animal_shop.community.post.entity.Wiki;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WikiDTO {

    private Long id;        // ID
    private String attachmentUrl;   // 첨부파일 URL
    private String breedName;       // 품종 이름
    private String overview;        // 개요
    private String appearance;      // 외모
    private String temperament;     // 성격
    private LocalDateTime created_Date; //등록날짜,시간

    public WikiDTO(Wiki wiki){
        this.id = wiki.getBreedId();
        this.breedName = wiki.getBreedName();
        this.overview = wiki.getOverview();
        this.appearance = wiki.getAppearance();
        this.temperament = wiki.getTemperament();
        this.created_Date = wiki.getCreatedDate();
    }

}
