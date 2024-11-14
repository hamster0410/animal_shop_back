package animal_shop.domain.post.service;


import animal_shop.domain.member.entity.Member;
import animal_shop.domain.member.service.MemberService;
import animal_shop.domain.post.dto.*;
import animal_shop.domain.post.entity.Post;
import animal_shop.domain.post.repository.PostRepository;
import animal_shop.global.security.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    @Value("${file.upload-dir-post}")  // 파일 저장 경로를 application.properties에 설정
    private String uploadDir;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private final PostRepository postRepository;

    @Autowired
    private MemberService memberService;


    //인기게시글 읽기
    @Transactional(readOnly = true)
    public List<PostListDTO> getBestPost(int page) {
        Pageable pageable = (Pageable) PageRequest.of(page,10);
        Page<Post> postPage = this.postRepository.findAllByOrderByHitsDesc(pageable);  // Post 페이지 가져오기
        // Post를 PostListDTO 변환
        List<PostListDTO> postListDTOList = new ArrayList<>();
        return postPage.getContent().stream()
                .map(PostListDTO::new)
                .collect(Collectors.toList());

    }

    @Transactional(readOnly = true)
    public List<PostListDTO> getNewPosts(int page) {
        Pageable pageable = (Pageable) PageRequest.of(page,10);
        Page<Post> postPage = this.postRepository.findAllByOrderByCreatedDateDesc(pageable);  // Post 페이지 가져오기

//         Post를 PostResponse로 변환
        return postPage.getContent().stream()
                .map(PostListDTO::new)
                .collect(Collectors.toList());

    }

    @Transactional(readOnly = true)
    public PostResponseDTO getCategoryPosts(int page, String category) {
        Pageable pageable = (Pageable) PageRequest.of(page,10);
        Page<Post> postPage = this.postRepository.findByCategory(pageable,category);
        // Post를 PostResponse로 변환
        List<PostListDTO> postListDTOList =postPage.getContent().stream()
                .map(PostListDTO::new)
                .collect(Collectors.toList());
        int postCount = (int) postPage.getTotalElements();

        return PostResponseDTO
                .builder()
                .posts(postListDTOList)
                .totalCount(postCount)
                .build();

    }

    @Transactional
    public PostResponseDTO getSearchPost(String about, String keyword, int page) {
        Pageable pageable = (Pageable) PageRequest.of(page,10);
        Page<Post> postPage = switch (about) {
            case "title" -> this.postRepository.findByTitleContaining(pageable, keyword);
            case "contents" -> this.postRepository.findByContentsContaining(pageable, keyword);
            case "nickname" -> this.postRepository.findByMemberNickname(pageable, keyword);
            default -> throw new IllegalArgumentException("Invalid search type: " + about);
        };

        // Post를 PostResponse로 변환
        List<PostListDTO> paging = postPage.getContent().stream()
                .map(PostListDTO::new)
                .collect(Collectors.toList());

        int postCount = (int) postPage.getTotalElements();

        return  PostResponseDTO
                .builder()
                .posts(paging)
                .totalCount(postCount)
                .build();

    }

    // 게시글 저장
    @Transactional
    public PostSaveDTO savePost(final PostRequest params, String token) throws IOException {
        String userId = tokenProvider.extractIdByAccessToken(token);
        Member member = memberService.getByUserId(Long.valueOf(userId));

        Post post = Post.builder()
                .title(params.getTitle())
                .member(member)
                .contents(params.getContents())
                .category(params.getCategory())
                .hits(0L)
                .build();
        postRepository.save(post);
        return post.toDto();
    }

    // 게시글 상세정보 조회
    @Transactional
    public PostDetailDTO checkByPostId(final Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("post not found : " + id));

        //조회수 증가 로직
        post.setHits(post.getHits()+1);
        postRepository.save(post);

        return new PostDetailDTO(post);
    }

    //현재 접속한 사람이 게시글을 작성한 사람인지 확인
    public boolean checkPostWriter(String token,  Long postId) {
        String userId = tokenProvider.extractIdByAccessToken(token);
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("post not found : " + postId));
        if(String.valueOf(post.getMember().getId()).equals(userId)){
           return true;
        }else{
            return false;
        }
    }

    //게시글 수정
    public PostSaveDTO updatePost(String token, Long postId, PostRequest params){

        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("post not found : " + postId));
        String userId = tokenProvider.extractIdByAccessToken(token);

        if(!Long.valueOf(userId).equals(post.getMember().getId())){
            throw new IllegalArgumentException("user is not writer");
        }
        post.setTitle(params.getTitle());
        post.setCategory(params.getCategory());
        post.setContents(params.getContents());
        postRepository.save(post);
        return post.toDto();
    }

    public void deletePost(String token, Long postId) {
        String userId = tokenProvider.extractIdByAccessToken(token);
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("post not found : " + postId));
        if(String.valueOf(post.getMember().getId()).equals(userId))
            postRepository.delete(post);
    }


    public Post getByPostId(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("post not found : " + postId));
        return  post;
    }

    public void increaseHeart(Post post) {
        post.setCountHeart(post.getCountHeart() + 1);
        postRepository.save(post);
    }


    public void decreaseHeart(Post post) {
        post.setCountHeart(post.getCountHeart() - 1);
        postRepository.save(post);
    }

    public void increaseComment(Post post) {
        post.setCountComment(post.getCountComment() + 1);
        postRepository.save(post);
    }


    public void decreaseComment(Post post) {
        post.setCountComment(post.getCountComment() - 1);
        postRepository.save(post);
    }

    public Long getPostCount() {
        return postRepository.count();
    }


}
