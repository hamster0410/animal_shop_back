package animal_shop.global.admin.service;

import animal_shop.community.member.entity.SellerCandidate;
import animal_shop.community.member.repository.SellerCandidateRepository;
import animal_shop.global.admin.dto.SellerDTO;
import animal_shop.global.admin.dto.SellerResponseDTO;
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

    public SellerResponseDTO request_list(int page) {
        System.out.println("check 2");
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
}
