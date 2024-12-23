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
public class SellerSignUpDTO {

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

    public SellerSignUpDTO(SellerSignUpDTO sellerSignUpDTO) {
        this.id = sellerSignUpDTO.getId();
        this.password = sellerSignUpDTO.getPassword();
        this.nickname = sellerSignUpDTO.getNickname();
        this.mail = sellerSignUpDTO.getMail();
        this.username = sellerSignUpDTO.getUsername();
        this.profile = sellerSignUpDTO.getProfile();
        this.category = sellerSignUpDTO.getCategory();
        this.contents = sellerSignUpDTO.getContents();
        this.phoneNumber = sellerSignUpDTO.getPhoneNumber();
        this.bln = sellerSignUpDTO.getBln();
    }

}
