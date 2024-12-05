        package animal_shop.tools.wiki_service.service;

        import animal_shop.community.member.entity.Member;
        import animal_shop.community.member.repository.MemberRepository;
        import animal_shop.global.security.TokenProvider;
        import animal_shop.tools.wiki_service.dto.WikiCommentDTO;
        import animal_shop.tools.wiki_service.entity.Wiki;
        import animal_shop.tools.wiki_service.entity.WikiComment;
        import animal_shop.tools.wiki_service.repository.WikiCommentRepository;
        import animal_shop.tools.wiki_service.repository.WikiRepository;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.stereotype.Service;
        import org.springframework.transaction.annotation.Transactional;


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
                System.out.println("here");

                Wiki wiki = wikiRepository.findById(wikiId)
                        .orElseThrow(() -> new IllegalArgumentException("Wiki not found"));
                System.out.println("here1");

                // 댓글 생성
                WikiComment comment = WikiComment.builder()
                        .content(content)  // content를 설정
                        .author(member.getUsername())  // 작성자 설정
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

                if (!comment.getAuthor().equals(member.getUsername())) {
                    throw new IllegalStateException("You can only delete your own comments");
                }

                wikiCommentRepository.delete(comment);
            }
        }
