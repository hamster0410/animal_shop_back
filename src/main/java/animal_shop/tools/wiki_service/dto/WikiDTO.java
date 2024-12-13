package animal_shop.tools.wiki_service.dto;


import animal_shop.tools.wiki_service.entity.Wiki;
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

    public WikiDTO(Wiki wiki){
        this.id = wiki.getId();
        this.attachmentUrl = wiki.getAttachmentUrl();;
        this.breedName = wiki.getBreedName();
        this.overview = wiki.getOverview();
        this.appearance = wiki.getAppearance();
        this.temperament = wiki.getTemperament();
    }

}