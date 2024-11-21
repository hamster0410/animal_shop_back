package animal_shop.global.admin.dto;

import lombok.*;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class SellerResponseDTO {
    private List<SellerDTO> sellerDTOS;
    private Long totalCount;

}
