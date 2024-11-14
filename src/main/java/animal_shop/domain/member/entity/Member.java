package animal_shop.domain.member.entity;

import animal_shop.global.dto.BaseTimeEntity;

import animal_shop.domain.comment.entity.Comment;
import animal_shop.domain.heart_post.entity.Heart;
import animal_shop.domain.member.Role;
import animal_shop.domain.post.entity.Post;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.boot.autoconfigure.task.TaskExecutionProperties;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
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
    private Role role;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts = new ArrayList<>(); // 컬렉션 초기화

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Heart> hearts = new ArrayList<>(); // 사용자가 누른 좋아요 리스트

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>(); // 사용자가 누른 좋아요 리스트


    //userDetails
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.role.name()));
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