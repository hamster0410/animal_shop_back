package animal_shop.tools.wiki_service.service;

import animal_shop.community.member.entity.Member;
import animal_shop.community.member.repository.MemberRepository;
import animal_shop.global.security.TokenProvider;
import animal_shop.tools.abandoned_animal.entity.AbandonedComment;
import animal_shop.tools.wiki_service.dto.WikiCommentDTO;
import animal_shop.tools.wiki_service.dto.WikiCommentResponse;
import animal_shop.tools.wiki_service.entity.Wiki;
import animal_shop.tools.wiki_service.entity.WikiComment;
import animal_shop.tools.wiki_service.repository.WikiCommentRepository;
import animal_shop.tools.wiki_service.repository.WikiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
public class WikiCommentService {

    @Autowired
    private WikiCommentRepository wikiCommentRepository;

    @Autowired
    private WikiRepository wikiRepository;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private MemberRepository memberRepository;

    @Transactional
    public void addComment(String token, Long wikiId,String content) {
        String userId = tokenProvider.extractIdByAccessToken(token);
        Member member = memberRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new IllegalArgumentException("member is not found"));

        Wiki wiki = wikiRepository.findById(wikiId)
                .orElseThrow(() -> new IllegalArgumentException("Wiki not found"));

        // 댓글 생성
        WikiComment comment = WikiComment.builder()
                .content(content)  // content를 설정
                .author(member.getNickname())  // 작성자 설정
                .wiki(wiki)  // 연결된 Wiki 설정
                .build();

// 댓글 저장
        wikiCommentRepository.save(comment);

    }
    @Transactional
    public void deleteComment(String token, Long commentId) {
        String userId = tokenProvider.extractIdByAccessToken(token);
        Member member = memberRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new IllegalArgumentException("member is not found"));

        WikiComment comment = wikiCommentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found"));

        if (!comment.getAuthor().equals(member.getNickname())) {
            throw new IllegalStateException("You can only delete your own comments");
        }

        wikiCommentRepository.delete(comment);
    }

    @Transactional(readOnly = true)
    public WikiCommentResponse listComment(String token,Long wikiId, int page) {
        Pageable pageable = (Pageable) PageRequest.of(page,20);
        Page<WikiComment> wikiCommentPage =wikiCommentRepository.findByWikiId(wikiId,pageable);
        List<WikiCommentDTO> wikiCommentDTOList = wikiCommentPage.stream().map(WikiCommentDTO::new).toList();
        long total_count = wikiCommentPage.getTotalElements();
        return WikiCommentResponse.builder()
                .wikiCommentDTOList(wikiCommentDTOList)
                .total_count(total_count)
                .build();

    }

    @Transactional
    public void updateComment(String token, Long commentId, WikiCommentDTO wikiCommentDTO) {
        String userId = tokenProvider.extractIdByAccessToken(token);
        Member member = memberRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new IllegalArgumentException("member is not found"));

        WikiComment wikiComment = wikiCommentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("WikiComment not found"));

        // 댓글 수정
        if(wikiCommentDTO.getContent()!= null){
            wikiComment.setContent(wikiCommentDTO.getContent());
        }
        if(wikiCommentDTO.getAuthor()!= null){
            wikiComment.setAuthor(wikiComment.getAuthor());
        }

        // 댓글 저장
        wikiCommentRepository.save(wikiComment);
    }

    public Boolean myComment(String token, Long commentId) {

        String userId = tokenProvider.extractIdByAccessToken(token);
        if(userId == null){
            throw new IllegalStateException("user is not found");
        }

        Member member = memberRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new IllegalArgumentException("member is not found"));

        //댓글 찾기
        WikiComment wikiComment = wikiCommentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("댓글이 존재하지 않습니다."));

        return wikiComment.getAuthor().equals(member.getNickname());

    }


}
