package animal_shop.tools.wiki_service.service;

import animal_shop.community.comment.repository.CommentRepository;
import animal_shop.community.member.entity.Member;
import animal_shop.community.member.repository.MemberRepository;
import animal_shop.tools.wiki_service.dto.WikiDTO;
import animal_shop.tools.wiki_service.dto.WikiDTOResponse;
import animal_shop.tools.wiki_service.entity.Wiki;
import animal_shop.tools.wiki_service.repository.WikiRepository;
import animal_shop.global.security.TokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
public class WikiService {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private WikiRepository wikiRepository;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private CommentRepository commentRepository;

    @Value("${file.upload-dir-wiki}")
    private String uploadDir;

    @Transactional
    public void wikiRegister(String token, WikiDTO wikiDTO) {
        //뉘신지 확인
        String userId = tokenProvider.extractIdByAccessToken(token);
        Member member = memberRepository.findById(Long.valueOf(userId))
                .orElseThrow(()->new IllegalArgumentException("member is not found"));
        //ADMIN인지 확인
        if(!member.getRole().toString().equals("ADMIN"))
            throw new IllegalStateException("User is not ADMIN");

        //등록
        Wiki wiki = new Wiki();
        wiki.setBreedName(wikiDTO.getBreedName());
        wiki.setOverview(wikiDTO.getOverview());
        wiki.setAppearance(wikiDTO.getAppearance());
        wiki.setTemperament(wikiDTO.getTemperament());
        wiki.setAttachmentUrl(wikiDTO.getAttachmentUrl());

//        if (file != null && !file.isEmpty()) {
//            try {
//                // 디렉토리가 없으면 생성
//                Files.createDirectories(Paths.get(uploadDir));
//                // 파일명 중복 방지 (UUID 사용)
//                String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
//                Path filePath = Paths.get(uploadDir, fileName);
//                Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
//                wiki.setAttachmentUrl(filePath.toString()); // 파일 경로 저장
//            } catch (IOException e) {
//                throw new RuntimeException("Failed to store file: " + e.getMessage(), e);
//            }
//        } else {
//            wiki.setAttachmentUrl(""); // 파일이 없을 경우 빈 값 설정
//        }

        wikiRepository.save(wiki);
    }
    @Transactional(readOnly = true)
    public WikiDTOResponse select(String token, int page) {

        Pageable pageable = (Pageable) PageRequest.of(page,10, Sort.by("createdDate").descending());

        Page<Wiki> wikiPage = wikiRepository.findAll(pageable);

        List<WikiDTO> wikiDTOList = wikiPage.stream().map(WikiDTO::new).toList();
        WikiDTOResponse wikiDTOResponse = WikiDTOResponse.builder()
                .wikiDTOList(wikiDTOList)
                .total_count(wikiPage.getTotalElements())
                .build();
        return wikiDTOResponse;

    }
    @Transactional(readOnly = true)
    public WikiDTOResponse selectDetail(String token, WikiDTO wikiDTO) {

        //ADMIN이 아닌 경우 예외 처리
        Wiki wiki = wikiRepository.findById(wikiDTO.getId())
                .orElseThrow(()->new IllegalArgumentException("Wiki not found"));

        List<WikiDTO> wikiDTOList = List.of(new WikiDTO(wiki));

        //빌더 패턴
        return WikiDTOResponse.builder()
                .wikiDTOList(wikiDTOList)
                .total_count(1L)
                .build();
    }


    public WikiDTOResponse selectMyDetail(String token, String breedName) {
        Wiki wiki = wikiRepository.findByBreedName(breedName)
                .orElseThrow(()->new IllegalArgumentException("Wiki not found"));

        List<WikiDTO> wikiDTOList = List.of(new WikiDTO(wiki));

        //빌더 패턴
        return WikiDTOResponse.builder()
                .wikiDTOList(wikiDTOList)
                .total_count(1L)
                .build();
    }

    @Transactional
    public void delete(String token, WikiDTO wikiDTO) {
        //토큰인증
        String userId = tokenProvider.extractIdByAccessToken(token);
        Member member = memberRepository.findById(Long.valueOf(userId))
                .orElseThrow(()->new IllegalArgumentException("member is not found"));
        //ADMIN이 아닌 경우 예외 처리
        if(!member.getRole().toString().equals("ADMIN")){
            throw new IllegalStateException("User is not ADMIN");
        }
        Wiki wiki = wikiRepository.findById(wikiDTO.getId())
                .orElseThrow(()->new IllegalArgumentException("Wiki not found"));

        wikiRepository.delete(wiki);
    }
//    @Transactional
//    public void update(String token, WikiDTO wikiDTO, MultipartFile file) {
//        //토큰인증
//        String userId = tokenProvider.extractIdByAccessToken(token);
//        Member member = memberRepository.findById(Long.valueOf(userId))
//                .orElseThrow(()->new IllegalArgumentException("member is not found"));
//        //ADMIN이 아닌 경우 예외 처리
////        if(!member.getRole().toString().equals("ADMIN")){
////            throw new IllegalStateException("User is not ADMIN");
////        }
//        Wiki wiki = wikiRepository.findById(wikiDTO.getId())
//                .orElseThrow(()->new IllegalArgumentException("Wiki not found"));
//
//
//        wiki.setAttachmentUrl(wikiDTO.getAttachmentUrl());
//        wiki.setBreedName(wikiDTO.getBreedName());
//        wiki.setOverview(wikiDTO.getOverview());
//        wiki.setAppearance(wikiDTO.getAppearance());
//        wiki.setTemperament(wikiDTO.getTemperament());
//
//
//        // 첨부파일 처리 (파일이 있을 경우)
//        if (file != null && !file.isEmpty()) {
//            try {
//                // 파일명 중복 방지 (UUID 사용)
//                String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
//                Path filePath = Paths.get(uploadDir, fileName);
//                Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
//                wiki.setAttachmentUrl(filePath.toString()); // 파일 경로 저장
//            } catch (IOException e) {
//                throw new RuntimeException("Failed to store file: " + e.getMessage(), e);
//            }
//        }
//        wikiRepository.save(wiki);
//    }
@Transactional
public void update(String token, WikiDTO wikiDTO, MultipartFile file) {
    // 토큰 인증
    String userId = tokenProvider.extractIdByAccessToken(token);
    Member member = memberRepository.findById(Long.valueOf(userId))
            .orElseThrow(() -> new IllegalArgumentException("member is not found"));

    if(!member.getRole().toString().equals("ADMIN")){
        throw new IllegalStateException("User is not ADMIN");
    }

    // Wiki 엔티티 조회
    Wiki wiki = wikiRepository.findById(wikiDTO.getId())
            .orElseThrow(() -> new IllegalArgumentException("Wiki not found"));

    // null이 아닌 필드만 업데이트
    if (wikiDTO.getAttachmentUrl() != null) {
        wiki.setAttachmentUrl(wikiDTO.getAttachmentUrl());
    }
    if (wikiDTO.getBreedName() != null) {
        wiki.setBreedName(wikiDTO.getBreedName());
    }
    if (wikiDTO.getOverview() != null) {
        wiki.setOverview(wikiDTO.getOverview());
    }
    if (wikiDTO.getAppearance() != null) {
        wiki.setAppearance(wikiDTO.getAppearance());
    }
    if (wikiDTO.getTemperament() != null) {
        wiki.setTemperament(wikiDTO.getTemperament());
    }

    // 첨부파일 처리 (파일이 있을 경우)
    if (file != null && !file.isEmpty()) {
        try {
            // 파일명 중복 방지 (UUID 사용)
            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(uploadDir, fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            wiki.setAttachmentUrl(filePath.toString()); // 파일 경로 저장
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file: " + e.getMessage(), e);
        }
    }

    // 저장
    wikiRepository.save(wiki);
    }
}