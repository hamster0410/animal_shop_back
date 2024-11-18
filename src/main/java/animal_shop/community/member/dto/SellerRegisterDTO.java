package animal_shop.community.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SellerRegisterDTO {

    private String category;
    private String contents;
    private String phone_number;
    private String BLN;

}
