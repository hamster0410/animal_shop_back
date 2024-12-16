package animal_shop.community.member.dto;

import animal_shop.community.member.Role;
import animal_shop.community.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberDTO {

    private Long id;

    private String password;

    private String nickname;

    private String mail;

    private String username;

    private String profile;

    private String AccessToken;

    private String RefreshToken;




    private Role role;

    public MemberDTO(Member member) {
        this.id = member.getId();
        this.password = member.getPassword();
        this.nickname = member.getNickname();
        this.mail = member.getMail();
        this.username = member.getUsername();
        this.profile = member.getProfile();
    }
}
