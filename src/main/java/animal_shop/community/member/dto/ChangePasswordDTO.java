package animal_shop.community.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordDTO {

    private String checkPassword;
    private String mail;
    private String newPassword;
}
