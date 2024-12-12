package animal_shop.global.service;

import animal_shop.community.member.entity.Member;
import animal_shop.community.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GlobalService {

    @Autowired
    MemberRepository memberRepository;

    public String getNickname(Long userId){
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("member is not found"));

        return member.getNickname();
    }
}
