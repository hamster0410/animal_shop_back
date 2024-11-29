package animal_shop.community.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VerifyMailDTO {
    private String mail;
    private String password;
    private String authentication; // 인증 관련 정보 (예: 인증 코드 등)

}
