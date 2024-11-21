package animal_shop.community.member.repository;

import animal_shop.community.member.entity.Member;
import animal_shop.community.member.entity.SellerCandidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SellerCandidateRepository extends JpaRepository <SellerCandidate,Long> {
    List<SellerCandidate> findByMember(Member member);
}
