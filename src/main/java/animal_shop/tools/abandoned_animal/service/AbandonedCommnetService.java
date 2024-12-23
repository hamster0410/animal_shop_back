package animal_shop.tools.abandoned_animal.service;

import animal_shop.community.member.entity.Member;
import animal_shop.community.member.repository.MemberRepository;
import animal_shop.global.security.TokenProvider;
import animal_shop.tools.abandoned_animal.dto.AbandonedCommentDTO;
import animal_shop.tools.abandoned_animal.dto.AbandonedCommentDTOResponse;
import animal_shop.tools.abandoned_animal.entity.AbandonedAnimal;
import animal_shop.tools.abandoned_animal.entity.AbandonedComment;
import animal_shop.tools.abandoned_animal.repository.AbandonedAnimalRepository;
import animal_shop.tools.abandoned_animal.repository.AbandonedCommentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class AbandonedCommnetService {

    @Autowired
    AbandonedAnimalRepository abandonedAnimalRepository;

    @Autowired
    AbandonedCommentRepository abandonedCommentRepository;

    @Autowired
    TokenProvider tokenProvider;

    @Autowired
    MemberRepository memberRepository;

    @Transactional
    public void addComment(String token, Long id, String content) {
        // JWT 토큰에서 userId 추출
        String userIdStr = tokenProvider.extractIdByAccessToken(token);
        Long userId = Long.valueOf(userIdStr);

        // userId로 회원 정보 조회
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

        // 회원의 username 추출
        String username = member.getNickname();  // username (사용자 이름) 추출

        // 동물 정보 조회
        AbandonedAnimal abandonedAnimal = abandonedAnimalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("동물 정보가 존재하지 않습니다."));

        // 댓글 생성 및 설정
        AbandonedComment comment = new AbandonedComment();
        comment.setAbandonedAnimal(abandonedAnimal);
        comment.setContent(content);
        comment.setUserId(userId); // userId 저장
        comment.setAuthor(username); // username을 author로 저장

        // 댓글 저장
        abandonedCommentRepository.save(comment);
    }
    @Transactional
    public void modifyComment(String token, Long id, String content) {

        //인증
        String userId = tokenProvider.extractIdByAccessToken(token);
        Member member = memberRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

        //댓글 찾기
        AbandonedComment abandonedComment = abandonedCommentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("댓글이 존재하지 않습니다."));
        if (!abandonedComment.getAuthor().equals(member.getNickname())) {
            throw new IllegalStateException("You can only delete your own comments");
        }
        // 5. 댓글 내용 수정
        abandonedComment.setContent(content);

        // 6. 댓글 저장 (자동으로 트랜잭션 관리)
        abandonedCommentRepository.save(abandonedComment);
    }
    @Transactional
    public void deleteComment(String token, Long id) {
        //인증
        String userId = tokenProvider.extractIdByAccessToken(token);
        Member member = memberRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));
        //댓글 찾기
        AbandonedComment abandonedComment = abandonedCommentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("댓글이 존재하지 않습니다."));
        if (!abandonedComment.getAuthor().equals(member.getNickname())) {
            throw new IllegalStateException("You can only delete your own comments");
        }
        abandonedCommentRepository.delete(abandonedComment);
    }

    @Transactional
    public AbandonedCommentDTOResponse getAllComments(Long id, int page) {
        // PageRequest 생성 (한 페이지에 20개씩 출력)
        Pageable pageable = PageRequest.of(page, 20);

        // 동물 정보 조회
        AbandonedAnimal abandonedAnimal = abandonedAnimalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("동물 정보가 존재하지 않습니다."));

        // 댓글 조회 및 Pagination 처리
        Page<AbandonedCommentDTO> commentPage = abandonedCommentRepository.findByAbandonedAnimal(abandonedAnimal,pageable);

        // 응답 데이터 생성
        return AbandonedCommentDTOResponse.builder()
                .abandonedCommentDTOList(commentPage.getContent()) // 댓글 리스트
                .total_count(commentPage.getTotalElements()) // 전체 댓글 개수
                .build();
    }

    public Boolean getMyComments(String token, Long commentId) {
        String userId = tokenProvider.extractIdByAccessToken(token);
        if(userId == null){
            throw new IllegalStateException("user is not found");
        }

        //댓글 찾기
        AbandonedComment abandonedComment = abandonedCommentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("댓글이 존재하지 않습니다."));

        return abandonedComment.getUserId().equals(Long.valueOf(userId));

    }
}
