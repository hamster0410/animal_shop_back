package animal_shop.community.member.service;

import animal_shop.community.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailService implements UserDetailsService {

    private MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username)  throws UsernameNotFoundException {
        return memberRepository.findByUsername(username).orElseThrow(()->new IllegalArgumentException((username)));
    }
}
