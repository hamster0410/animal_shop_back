package animal_shop.community.member.repository;

import animal_shop.community.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUsername(String username);

    boolean existsByUsername(String username);

    Member findByUsernameAndPassword(String username, String password);

    boolean existsByMail(String mail);

    boolean existsByNickname(String nickname);

    Member findByNickname(String seller);

    boolean existsByPhoneNumber(String phoneNumber);

    boolean existsByBln(String bln);

    @Modifying
    @Query("UPDATE Member m SET m.authentication = :authentication WHERE m.mail = :email")
    void updatePasswordByEmail(@Param("email") String email, @Param("authentication") String authentication);

    @Query("SELECT m.authentication FROM Member m WHERE m.mail = :email")
    String findAuthentication(@Param("email") String email);

    Optional<Member> findByMail(String email);


}
