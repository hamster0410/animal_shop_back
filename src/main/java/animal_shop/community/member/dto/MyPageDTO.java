package animal_shop.community.member.dto;

import animal_shop.community.member.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyPageDTO {
    private Long id;

    private String nickname;

    private String mail;

    private String username;

    private String profile;

    private String petProfile;

    private String petName;

    private Role role;
}
