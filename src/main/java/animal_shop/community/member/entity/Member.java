package animal_shop.community.member.entity;

import animal_shop.global.dto.BaseTimeEntity;

import animal_shop.community.comment.entity.Comment;
import animal_shop.community.heart_post.entity.Heart;
import animal_shop.community.member.Role;
import animal_shop.community.post.entity.Post;
import animal_shop.shop.item_comment.entity.ItemComment;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collection;
import java.util.List;

@Table(name = "MEMBER")
@Getter
@NoArgsConstructor
@Entity
@AllArgsConstructor
@Builder
public class Member extends BaseTimeEntity implements UserDetails {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="member_id")
    private Long id;

    @Setter
    private String password;

    @Column(nullable = false, length = 30, unique = true)
    private String username;

    @Setter
    @Column(nullable = false, length = 30, unique = true)
    private String mail;

    @Setter
    @Column(nullable = false, length = 30, unique = true)
    private String nickname;

    private String refreshtoken;

    @Enumerated(EnumType.STRING)
    @Setter
    private Role role;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Heart> hearts;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemComment> item_comments;

    private String profile;

    //userDetails
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + this.role.name()));
    }

    @Override
    public String getUsername(){return username;}

    @Override
    public String getPassword(){return password;}

    public void updateUserName(String username){
        this.username = username;
    }

    public void updatePassword(PasswordEncoder passwordEncoder, String password){
        this.password = passwordEncoder.encode(password);
    }


    public void updateRefreshToken(String refreshtoken){
        this.refreshtoken = refreshtoken;
    }

    public void encodePassword(PasswordEncoder passwordEncoder){
        this.password = passwordEncoder.encode(password);
    }

    public void updateRole(Role role) {
        this.role = role;
    }

}