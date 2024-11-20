package animal_shop.shop.item_comment_like.service;

import animal_shop.community.member.entity.Member;
import animal_shop.community.member.service.MemberService;
import animal_shop.global.security.TokenProvider;
import animal_shop.shop.item_comment.entity.ItemComment;
import animal_shop.shop.item_comment.repository.ItemCommentRepository;
import animal_shop.shop.item_comment_like.entity.ItemCommentLike;
import animal_shop.shop.item_comment_like.repository.ItemCommentLikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ItemCommentLikeService {
    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private MemberService memberService;

    @Autowired
    private ItemCommentLikeRepository itemCommentLikeRepository;

    @Autowired
    private ItemCommentRepository itemCommentRepository;


    public void addHeart(String token, Long commentId) {
        //member 찾기
        String userId = tokenProvider.extractIdByAccessToken(token);
        Member member = memberService.getByUserId(Long.valueOf(userId));

        //Comment 찾기
        ItemComment itemComment = itemCommentRepository.findById(commentId).orElseThrow(()-> new IllegalArgumentException("comment not found"));


        itemComment.setCountHeart(itemComment.getCountHeart() + 1);
        itemCommentRepository .save(itemComment);

        ItemCommentLike commentLike = ItemCommentLike.builder()
                .member(member)
                .itemComment(itemComment)
                .build();

        itemCommentLikeRepository.save(commentLike);
    }

    public void deleteHeart(String token, Long commentId) {
        //member 찾기
        String userId = tokenProvider.extractIdByAccessToken(token);
        Member member = memberService.getByUserId(Long.valueOf(userId));

        //Comment 찾기
        ItemComment itemComment = itemCommentRepository.findById(commentId).orElseThrow(()-> new IllegalArgumentException("comment not found"));


        itemComment.setCountHeart(itemComment.getCountHeart() - 1);
        itemCommentRepository .save(itemComment);

        ItemCommentLike itemCommentLike = itemCommentLikeRepository.findByItemCommentIdAndMemberId(commentId, Long.valueOf(userId));

        itemCommentLikeRepository.delete(itemCommentLike);
    }

}
