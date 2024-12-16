package animal_shop.community.post.service;


import animal_shop.community.member.entity.Member;
import animal_shop.community.member.repository.MemberRepository;
import animal_shop.community.post.dto.NoticeDTOResponse;
import animal_shop.community.post.dto.NoticesDTO;
import animal_shop.community.post.entity.Notices;
import animal_shop.community.post.repository.NoticesRepository;
import animal_shop.global.security.TokenProvider;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j

public class NoticesService {

    @Autowired
    private NoticesRepository noticesRepository;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private MemberRepository memberRepository;

    @Value("${file.upload-dir-notices}")
    private String uploadDir;


    @Transactional
    public void admin_register(String token, NoticesDTO noticesDTO, MultipartFile file) {
        //토큰 인증
        String userId = tokenProvider.extractIdByAccessToken(token);

        Member member = memberRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new IllegalArgumentException("member is not found"));
        // ADMIN 아닌 경우 예외 처리
        if (!member.getRole().toString().equals("ADMIN")) {
            throw new IllegalStateException("User is not ADMIN");
        }

        Notices notices = new Notices();
        notices.setTitle(noticesDTO.getTitle());
        notices.setContent(noticesDTO.getContent());
        notices.setSenderId(member.getId()); // 발송자 ID 설정
        notices.setPriority(noticesDTO.getPriority());

        if (file != null && !file.isEmpty()) {
            try {
                // 파일명 중복 방지 (UUID 사용)
                String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
                Path filePath = Paths.get(uploadDir, fileName);
                Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                notices.setAttachmentUrl(filePath.toString()); // 파일 경로 저장
            } catch (IOException e) {
                throw new RuntimeException("Failed to store file: " + e.getMessage(), e);
            }
        } else {
            notices.setAttachmentUrl(""); // 파일이 없을 경우 빈 값 설정
        }

        //공지사항 저장
        noticesRepository.save(notices);
    }

    //전체 조히
    @Transactional(readOnly = true)
    public NoticeDTOResponse adminSearch(String token, int page) {

        //토큰 인증
        String userId = tokenProvider.extractIdByAccessToken(token);

        Member member = memberRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new IllegalArgumentException("member is not found"));
        // ADMIN 아닌 경우 예외 처리
        if (member.getRole().toString().equals("USER")) {
            throw new IllegalStateException("User is not ADMIN");
        }
        // Pageable 설정 (페이지 당 10개로 제한)
        Pageable pageable = PageRequest.of(page, 10, Sort.by("createdDate").descending());

        Page<Notices> noticesPage = noticesRepository.findAllByOrderByPriorityAsc(pageable);

        List<NoticesDTO> noticesDTOList = noticesPage.stream().map(NoticesDTO::new).toList();
        NoticeDTOResponse noticeDTOResponse = NoticeDTOResponse.builder()
                .noticesDTOList(noticesDTOList)
                .total_count(noticesPage.getTotalElements())
                .build();

        return noticeDTOResponse;

    }

    @Transactional(readOnly = true)
    public NoticeDTOResponse adminSearchId(String token, NoticesDTO noticesDTO) {

        // 인증
        String userId = tokenProvider.extractIdByAccessToken(token);

        // ADMIN 권한 확인
        Member member = memberRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new IllegalArgumentException("member is not found"));
        if (member.getRole().toString().equals("USER")) {
            throw new IllegalStateException("User is not ADMIN");
        }

        // 특정 공지사항 조회
        Notices notice = noticesRepository.findById(noticesDTO.getId())
                .orElseThrow(() -> new IllegalArgumentException("Notice not found"));

        // 공지사항 DTO로 변환
        List<NoticesDTO> noticesList = List.of(new NoticesDTO(notice));

        // 빌더 패턴을 사용하여 객체 생성
        return NoticeDTOResponse.builder()
                .noticesDTOList(noticesList)
                .total_count(1L) // 총 개수 1 설정
                .build();

    }

    @Transactional
    public void delete(String token, NoticesDTO noticesDTO) {
        // 인증 및 ADMIN 권한 확인
        String userId = tokenProvider.extractIdByAccessToken(token);
        Member member = memberRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new IllegalArgumentException("member is not found"));

        if (!member.getRole().toString().equals("ADMIN")) {
            throw new IllegalStateException("User is not ADMIN");
        }

        // 특정 공지사항 조회
        Notices notice = noticesRepository.findById(noticesDTO.getId())
                .orElseThrow(() -> new IllegalArgumentException("Notice not found"));

        noticesRepository.delete(notice);
    }

    @Transactional
    public void update(String token, NoticesDTO noticesDTO, MultipartFile file) {
        // 토큰을 사용하여 사용자 인증
        String userId = tokenProvider.extractIdByAccessToken(token);
        Member member = memberRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new IllegalArgumentException("member is not found"));

        // ADMIN 권한 확인
        if (!member.getRole().toString().equals("ADMIN")) {
            throw new IllegalStateException("User is not ADMIN");
        }
        // 특정 공지사항 조회
        Notices notice = noticesRepository.findById(noticesDTO.getId())
                .orElseThrow(() -> new IllegalArgumentException("Notice not found"));

        // 공지사항 수정 (noticesDTO로부터 새로운 값 설정)
        notice.setTitle(noticesDTO.getTitle());
        notice.setContent(noticesDTO.getContent());
        notice.setPriority(noticesDTO.getPriority());

        // 첨부파일 처리 (파일이 있을 경우)
        if (file != null && !file.isEmpty()) {
            try {
                // 파일명 중복 방지 (UUID 사용)
                String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
                Path filePath = Paths.get(uploadDir, fileName);
                Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                notice.setAttachmentUrl(filePath.toString()); // 파일 경로 저장
            } catch (IOException e) {
                throw new RuntimeException("Failed to store file: " + e.getMessage(), e);
            }
        }

        // 공지사항을 DB에 저장 (변경된 내용)
        noticesRepository.save(notice);
    }


}
