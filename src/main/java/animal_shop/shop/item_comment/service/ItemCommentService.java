package animal_shop.shop.item_comment.service;

import animal_shop.community.member.entity.Member;
import animal_shop.community.member.service.MemberService;
import animal_shop.global.security.TokenProvider;
import animal_shop.shop.item.entity.Item;
import animal_shop.shop.item.repository.ItemRepository;
import animal_shop.shop.item_comment.dto.ItemCommentDTO;
import animal_shop.shop.item_comment.dto.ItemCommentDTOResponse;
import animal_shop.shop.item_comment.dto.RequsetItemCommentDTO;
import animal_shop.shop.item_comment.entity.ItemComment;
import animal_shop.shop.item_comment.repository.ItemCommentRepository;
import animal_shop.shop.item_comment_like.entity.ItemCommentLike;
import animal_shop.shop.item_comment_like.repository.ItemCommentLikeRepository;
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
    ItemCommentRepository itemCommentRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    TokenProvider tokenProvider;

    @Autowired
    MemberService memberService;

    @Autowired
    ItemCommentLikeRepository itemCommentLikeRepository;

    public ItemCommentDTOResponse getCommentsByItemId(Long itemId, String token, int page) {

        List<ItemCommentDTO> commentDTOS = new ArrayList<>();
        Pageable pageable = (Pageable) PageRequest.of(page,20);

        Item item = itemRepository.findById(itemId).orElseThrow(() -> new IllegalArgumentException("item not found"));

        //해당 상품의 댓글들 조회
        Page<ItemComment> comments = itemCommentRepository.findByItem(item, pageable);

        //댓글 좋아요 기능
            if(token!=null){
                String userId = tokenProvider.extractIdByAccessToken(token);
                for(ItemComment comment : comments){
                    ItemCommentDTO commentDTO = new ItemCommentDTO(comment);
                    ItemCommentLike commentLike = itemCommentLikeRepository.findByItemCommentIdAndMemberId(comment.getId(), Long.valueOf(userId));
                    commentDTO.setHeart(commentLike!=null);
                    commentDTOS.add(commentDTO);
                }
            }else{
            commentDTOS = comments.stream()
                    .map(ItemCommentDTO::new)  // Comment 객체를 CommentDTO로 변환
                    .toList();

        }


        return ItemCommentDTOResponse
                .builder()
                .comments(commentDTOS)
                .total_count(comments.getTotalElements())
                .build();

    }

    public void createComment(String token, Long itemId, RequsetItemCommentDTO requestItemCommentDTO) {
        String userId = tokenProvider.extractIdByAccessToken(token);

        Member member = memberService.getByUserId(Long.valueOf(userId));

        Item item = itemRepository.findById(itemId).orElseThrow(() -> new IllegalArgumentException("item not found"));

        ItemComment comment = ItemComment.builder()
                .contents(requestItemCommentDTO.getContents())
                .item(item)
                .rating(requestItemCommentDTO.getRating())
                .countHeart(0L)
                .thumbnail_url(requestItemCommentDTO.getThumbnailUrls())
                .member(member)
                .build();

        item.setComment_count(item.getComment_count()+1);
        itemRepository.save(item);

        itemCommentRepository.save(comment);
    }

    public ItemCommentDTO updateComment(String token, Long commentId, RequsetItemCommentDTO requestItemCommentDTO) {
        String userId = tokenProvider.extractIdByAccessToken(token);

        ItemComment comment = itemCommentRepository.findById(commentId).orElseThrow(()-> new IllegalArgumentException("comment not found"));
        comment.setContents(requestItemCommentDTO.getContents());
        comment.setRating(requestItemCommentDTO.getRating());
        comment.setThumbnail_url(requestItemCommentDTO.getThumbnailUrls());

        itemCommentRepository.save(comment);

        return new ItemCommentDTO(comment);
    }

    public void deleteComment(String token, Long commentId) {
        Long userId = Long.valueOf(tokenProvider.extractIdByAccessToken(token));
        ItemComment comment = itemCommentRepository.findById(commentId).orElseThrow(() -> new IllegalArgumentException("comment not found"));

        Item item = comment.getItem();
        item.setComment_count(item.getComment_count()-1);
        itemRepository.save(item);

        if(userId.equals(comment.getMember().getId())){
            itemCommentRepository.delete(comment);
        }else{
            throw new IllegalArgumentException("comment is not present");
        }
    }

    public boolean checkCommentWriter(String token, Long commentId) {
        String userId = tokenProvider.extractIdByAccessToken(token);
        ItemComment comment = itemCommentRepository.findById(commentId).orElseThrow(() -> new IllegalArgumentException("comment not found : " + commentId));
        if(String.valueOf(comment.getMember().getId()).equals(userId)){
            return true;
        }else{
            return false;
        }
    }

}
