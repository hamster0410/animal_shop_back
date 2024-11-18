package animal_shop.global.admin.service;

import animal_shop.community.member.Role;
import animal_shop.community.member.entity.Member;
import animal_shop.community.member.entity.SellerCandidate;
import animal_shop.community.member.repository.MemberRepository;
import animal_shop.community.member.repository.SellerCandidateRepository;
import animal_shop.global.admin.dto.SellerDTO;
import animal_shop.global.admin.dto.SellerResponseDTO;
import animal_shop.global.security.TokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminService {

    @Autowired
    private SellerCandidateRepository sellerCandidateRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    TokenProvider tokenProvider;

    public SellerResponseDTO request_list(int page) {
        Pageable pageable = (Pageable) PageRequest.of(page,10);
        Page<SellerCandidate> sellerCandidates = sellerCandidateRepository.findAll(pageable);

       List<SellerDTO> sellerDTOS = sellerCandidates.getContent().stream()
                .map(SellerDTO::new)
                .collect(Collectors.toList());

       return  SellerResponseDTO
               .builder()
               .sellerDTOS(sellerDTOS)
               .totalCount(sellerCandidates.getTotalElements())
               .build();
    }

    public void permitSeller(String token, String username) {
        String userId = tokenProvider.extractIdByAccessToken(token);
        Member admin = memberRepository.findById(Long.valueOf(userId)).orElseThrow(() -> new IllegalArgumentException("Member does not exist with ID: "));
        System.out.println(username);
        Member user = memberRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("Member does not exist with USER NAME: "+username));

        //admin인지 아닌지 판별
        if(!admin.getRole().toString().equals("ADMIN")){
            throw new IllegalArgumentException("member is not admin ");
        }

        user.setRole(Role.SELLER);
        memberRepository.save(user);
    }
}
