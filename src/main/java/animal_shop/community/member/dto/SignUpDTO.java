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
public class SignUpDTO {

    private Long id;

    private String password;

    private String nickname;

    private String mail;

    private String username;

    private String profile;

    private String AccessToken;

    private String RefreshToken;

    private Role role;

    private String category;

    private String contents;

    private String phoneNumber;

    private String bln;

    public SignUpDTO(SignUpDTO signUpDTO) {
        this.id = signUpDTO.getId();
        this.password = signUpDTO.getPassword();
        this.nickname = signUpDTO.getNickname();
        this.mail = signUpDTO.getMail();
        this.username = signUpDTO.getUsername();
        this.profile = signUpDTO.getProfile();
        this.category = signUpDTO.getCategory();
        this.contents = signUpDTO.getContents();
        this.phoneNumber = signUpDTO.getPhoneNumber();
        this.bln = signUpDTO.getBln();
    }

}
