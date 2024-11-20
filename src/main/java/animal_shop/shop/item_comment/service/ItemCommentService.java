package animal_shop.shop.item_comment.service;

import animal_shop.community.comment.dto.CommentDTO;
import animal_shop.community.comment.dto.CommentResponseDTO;
import animal_shop.community.comment.entity.Comment;
import animal_shop.community.heart_comment.entity.CommentHeart;
import animal_shop.community.member.entity.Member;
import animal_shop.community.member.service.MemberService;
import animal_shop.community.post.entity.Post;
import animal_shop.global.security.TokenProvider;
import animal_shop.shop.item.entity.Item;
import animal_shop.shop.item.repository.ItemRepository;
import animal_shop.shop.item_comment.dto.ItemCommentDTO;
import animal_shop.shop.item_comment.dto.ItemCommentDTOResponse;
import animal_shop.shop.item_comment.dto.RequsetItemCommentDTO;
import animal_shop.shop.item_comment.entity.ItemComment;
import animal_shop.shop.item_comment.repository.ItemCommentReposiotry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ItemCommentService {

    @Autowired
    ItemCommentReposiotry itemCommentReposiotry;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    TokenProvider tokenProvider;

    @Autowired
    MemberService memberService;

    public ItemCommentDTOResponse getCommentsByItemId(Long itemId, String token, int page) {

        List<ItemCommentDTO> commentDTOS = new ArrayList<>();
        Pageable pageable = (Pageable) PageRequest.of(page,20);

        Item item = itemRepository.findById(itemId).orElseThrow(() -> new IllegalArgumentException("item not found"));

        //해당 상품의 댓글들 조회
        Page<ItemComment> comments = itemCommentReposiotry.findByItem(item, pageable);

        //댓글 좋아요 기능
//        if(token!=null){
//            String userId = tokenProvider.extractIdByAccessToken(token);
//            for(Comment comment : comments){
//                CommentDTO commentDTO = new CommentDTO(comment);
//                CommentHeart commentheart = commentHeartRepository.findByMemberIdAndCommentId(comment.getId(), Long.valueOf(userId));
//                commentDTO.setHeart(commentheart!=null);
//                commentDTOS.add(commentDTO);
//            }
//        }else{
            commentDTOS = comments.stream()
                    .map(ItemCommentDTO::new)  // Comment 객체를 CommentDTO로 변환
                    .toList();

//        }


        return ItemCommentDTOResponse
                .builder()
                .comments(commentDTOS)
                .total_count(comments.getTotalElements())
                .build();

    }

    public void createComment(String token, Long itemId, RequsetItemCommentDTO requestItemCommentDTO) {
        String userId = tokenProvider.extractIdByAccessToken(token);
        Member member = memberService.getByUserId(Long.valueOf(userId));
        System.out.println("here1");
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new IllegalArgumentException("item not found"));
        System.out.println("here3");
        ItemComment comment = ItemComment.builder()
                .contents(requestItemCommentDTO.getContents())
                .item(item)
                .rating(requestItemCommentDTO.getRating())
                .countHeart(0L)
                .member(member)
                .build();
        System.out.println("here3");
        item.setComment_count(item.getComment_count()+1);
        System.out.println("here4");
        itemCommentReposiotry.save(comment);
    }

}
