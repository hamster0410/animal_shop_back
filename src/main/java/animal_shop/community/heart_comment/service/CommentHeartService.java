package animal_shop.community.heart_comment.service;

import animal_shop.community.comment.entity.Comment;
import animal_shop.community.comment.service.CommentService;
import animal_shop.community.heart_comment.entity.CommentHeart;
import animal_shop.community.heart_comment.repository.CommentHeartRepository;
import animal_shop.community.member.entity.Member;
import animal_shop.community.member.service.MemberService;
import animal_shop.global.security.TokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CommentHeartService {

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private MemberService memberService;

    @Autowired
    private CommentHeartRepository commentHeartRepository;

    @Autowired
    private CommentService commentService;

    public void addHeart(String token, Long commentId) {
        //member 찾기
        String userId = tokenProvider.extractIdByAccessToken(token);
        Member member = memberService.getByUserId(Long.valueOf(userId));

        //Comment 찾기
        Comment comment = commentService.getByCommentId(commentId);

        commentService.increaseHeart(comment);

        CommentHeart commentHeart = CommentHeart.builder()
                .member(member)
                .comment(comment)
                .build();

        commentHeartRepository.save(commentHeart);
    }

    public void deleteHeart(String token, Long commentId) {
        System.out.println("[HeartService] deleteHeart");
        //member 찾기
        String userId = tokenProvider.extractIdByAccessToken(token);

        //post 찾기
        Comment comment = commentService.getByCommentId(commentId);

        commentService.decreaseHeart(comment);


        CommentHeart commentHeart = commentHeartRepository.findByMemberIdAndCommentId(commentId, Long.valueOf(userId));

        commentHeartRepository.delete(commentHeart);
    }

    public boolean findByCommentAndMember(Long commentId, String userId) {
        CommentHeart commentHeart = commentHeartRepository.findByMemberIdAndCommentId(commentId, Long.valueOf(userId));
        return commentHeart != null;
    }
}
