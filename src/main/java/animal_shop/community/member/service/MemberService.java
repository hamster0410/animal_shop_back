package animal_shop.community.member.service;

import animal_shop.community.comment.dto.CommentDTO;
import animal_shop.community.comment.dto.CommentResponseDTO;
import animal_shop.community.comment.entity.Comment;
import animal_shop.community.comment.repository.CommentRepository;
import animal_shop.community.heart_comment.entity.CommentHeart;
import animal_shop.community.heart_comment.repository.CommentHeartRepository;
import animal_shop.community.heart_post.entity.Heart;
import animal_shop.community.heart_post.repository.HeartRepository;
import animal_shop.community.member.Role;
import animal_shop.community.member.dto.*;
import animal_shop.community.member.entity.Member;
import animal_shop.community.member.entity.SellerCandidate;
import animal_shop.community.member.repository.MemberRepository;
import animal_shop.community.member.repository.SellerCandidateRepository;
import animal_shop.community.post.dto.PostListDTO;
import animal_shop.community.post.dto.PostResponseDTO;
import animal_shop.community.post.entity.Post;
import animal_shop.community.post.repository.PostRepository;
import animal_shop.global.security.TokenProvider;
import animal_shop.global.service.GlobalService;
import animal_shop.shop.item.dto.QueryResponse;
import animal_shop.shop.item.dto.ResponseItemQueryDTO;
import animal_shop.shop.item.entity.ItemQuery;
import animal_shop.shop.item.repository.ItemQueryRepository;
import animal_shop.shop.item_comment.dto.ItemCommentDTO;
import animal_shop.shop.item_comment.dto.ItemCommentDTOResponse;
import animal_shop.shop.item_comment.entity.ItemComment;
import animal_shop.shop.item_comment.repository.ItemCommentRepository;
import animal_shop.shop.item_comment_like.entity.ItemCommentLike;
import animal_shop.shop.item_comment_like.repository.ItemCommentLikeRepository;
import animal_shop.shop.pet.entity.Pet;
import animal_shop.tools.map_service.dto.MapPositionDTO;
import animal_shop.tools.map_service.dto.MapPositionDTOResponse;
import animal_shop.tools.map_service.entity.MapEntity;
import animal_shop.tools.map_service.entity.MapLike;
import animal_shop.tools.map_service.repository.MapLikeRepository;
import animal_shop.tools.wiki_service.repository.WikiRepository;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.*;

@Service
@Slf4j
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MapLikeRepository mapLikeRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private HeartRepository heartRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private CommentHeartRepository commentHeartRepository;

    @Autowired
    private ItemQueryRepository itemQueryRepository;

    @Autowired
    private ItemCommentRepository itemCommentRepository;

    @Autowired
    private ItemCommentLikeRepository itemCommentLikeRepository;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private SellerCandidateRepository sellerCandidateRepository;

    @Autowired
    private WikiRepository wikiRepository;

    @Autowired
    private JavaMailSenderImpl javaMailSenderImpl;

    @Autowired
    private GlobalService globalService;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Transactional
    public int create(MemberDTO memberDTO) {
        //동일 id 검사
        if (memberRepository.existsByUsername(memberDTO.getUsername())) {
            log.warn("Username already exists {}", memberDTO.getUsername());
            return 1;
        }

        //동일 메일 검사
        if (memberRepository.existsByMail(memberDTO.getMail())) {
            log.warn("Mail already exists {}", memberDTO.getMail());
            return 2;
        }

        //동일 닉네임 검사
        if (memberRepository.existsByNickname(memberDTO.getNickname())) {
            log.warn("Nickname already exists {}", memberDTO.getNickname());
            return 3;
        }

        Member member = Member.builder()
                .username(memberDTO.getUsername())
                .password(passwordEncoder.encode((memberDTO.getPassword())))
                .mail(memberDTO.getMail())
                .nickname(memberDTO.getNickname())
                .profile(memberDTO.getProfile())
                .role(Role.ADMIN)
                .build();

        memberRepository.save(member);

        return 0;
    }

    @Transactional
    public TokenDTO login(MemberDTO mDTO) {

        final String AccessToken = tokenProvider.AccessTokenCreate(String.valueOf(mDTO.getId()));
        final String RefreshToken = tokenProvider.RefreshTokenCreate(mDTO);
        saveRefreshToken(mDTO, RefreshToken);
        return TokenDTO.builder().AccessToken(AccessToken).RefreshToken(RefreshToken).build();
    }

    @Transactional
    public void modify(MemberDTO memberDTO, String token) {

        Long userId = Long.valueOf(tokenProvider.extractIdByAccessToken(token));
        Optional<Member> member = memberRepository.findById(userId);

        if (!memberDTO.getMail().equals(member.get().getMail()) && memberRepository.existsByMail(memberDTO.getMail())) {
            log.warn("Mail already exists {}", memberDTO.getMail());
            throw new RuntimeException("mail already exists");
        }
        if (!memberDTO.getNickname().equals(member.get().getNickname()) && memberRepository.existsByNickname(memberDTO.getNickname())) {
            log.warn("Nickname already exists {}", memberDTO.getNickname());
            throw new RuntimeException("Nickname already exists");
        }
        if (member.isPresent()) {
            if (memberDTO.getMail() != null) {
                member.get().setMail(memberDTO.getMail());
            }
            if (memberDTO.getNickname() != null) {
                member.get().setNickname(memberDTO.getNickname());
            }
            if (memberDTO.getProfile() != null){
                member.get().setProfile(memberDTO.getProfile());
            }

            memberRepository.save(member.get());
        }
    }

    @Transactional
    public void delete(String token) {
        System.out.println("Member Service delete");
        Long userId = Long.valueOf(tokenProvider.extractIdByAccessToken(token));
        Optional<Member> member = memberRepository.findById(userId);
        if (member.isPresent()) {
            memberRepository.delete(member.get());
        } else {
            throw new RuntimeException("User is not exists");
        }
    }

    @Transactional
    public MemberDTO getByCredentials(final MemberDTO memberDTO) {
        System.out.println("Member Service getByCredentials");
        final Optional<Member> originalMember = memberRepository.findByUsername(memberDTO.getUsername());
        if (originalMember.isPresent() && passwordEncoder.matches(memberDTO.getPassword(), originalMember.get().getPassword())) {
            System.out.println("member is present and pw is matching");

            return MemberDTO.builder()
                    .id(originalMember.get().getId())
                    .username(memberDTO.getUsername())
                    .build();
        }
        return null;
    }

    @Transactional
    public void saveRefreshToken(MemberDTO mDTO, String refreshToken) {
        System.out.println("Member Service saveRefreshToken");
        final Optional<Member> originalMember = memberRepository.findByUsername(mDTO.getUsername());

        if (originalMember.isPresent()) {
            originalMember.get().updateRefreshToken(refreshToken);
            memberRepository.save(originalMember.get());
        }
    }

    @Transactional
    public Member getByUserId(Long userId) {
        System.out.println("Member Service getByUserId");
        Optional<Member> member = memberRepository.findById(userId);
        return member.orElse(null);
    }

    @Transactional
    public MyPageDTO getByToken(String token) {
        System.out.println("Member Service getByToken");
        Long userId = Long.valueOf(tokenProvider.extractIdByAccessToken(token));
        Optional<Member> member = memberRepository.findById(userId);
        if (member.isPresent()) {
            String petProfile = null;
            Long petWikiId= null;
            for(Pet p : member.get().getPets()){
                if(p.getLeader()){
                    petProfile = p.getProfileImageUrl();
                    System.out.println(p.getBreed());
                    petWikiId = wikiRepository.findByBreedName(p.getBreed()).get().getId();
                    System.out.println(petWikiId);

                }
            }
            return MyPageDTO.builder()
                    .id(member.get().getId())
                    .username(member.get().getUsername())
                    .mail(member.get().getMail())
                    .nickname(member.get().getNickname())
                    .profile(member.get().getProfile())
                    .petProfile(petProfile)
                    .role(member.get().getRole())
                    .petWikiId(petWikiId)
                    .build();
        }
        return null;
    }

    public MemberDTO getBymail(String email) {
        Member member = memberRepository.findByMail(email)
                .orElseThrow(() -> new IllegalArgumentException("member is not found"));
        return new MemberDTO(member);
    }

    @Transactional
    public TokenDTO getNewAccessToken(TokenDTO tokenDTO) {
        System.out.println("Member Service getNewAccessToken");

        String refreshToken = tokenDTO.getRefreshToken();
        String memberId = tokenProvider.extractIdByRefreshToken(refreshToken);
        Member member = getByUserId(Long.valueOf(memberId));

        if (member.getRefreshtoken().equals(refreshToken)) {
            final TokenDTO token = TokenDTO.builder()
                    .AccessToken(tokenProvider.AccessTokenCreate(tokenProvider.extractIdByRefreshToken(refreshToken)))
                    .build();

            return token;
        }

        throw new RuntimeException("Invalid Refresh Token");
    }


    @Transactional
    public void enroll_seller(String token, SellerRegisterDTO sellerRegisterDTO) {
        String userId = tokenProvider.extractIdByAccessToken(token);

        Member member = memberRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new IllegalArgumentException("Member does not exist with ID: "));

        if (!sellerCandidateRepository.findByMember(member).isEmpty()) {
            throw new IllegalArgumentException("Member is already Seller " + member.getNickname());
        }
        SellerCandidate s = SellerCandidate.builder()
                .member(member)
                .category(sellerRegisterDTO.getCategory())
                .contents(sellerRegisterDTO.getContents())
                .phone_number(sellerRegisterDTO.getPhone_number())
                .bln(sellerRegisterDTO.getBln())
                .build();

        sellerCandidateRepository.save(s);
    }
    @Transactional
    public void createAndSendNewPassword(SendMailDTO sendMailDTO) {
        try {
            // 새로운 비밀번호 생성
            String newPassword = createNewPassword();

            // 비밀번호가 생성되면 이메일로 전송
            if (newPassword != null && !newPassword.isEmpty()) {
                sendNewPasswordByMail(sendMailDTO.getToMailAddr(), newPassword);
            } else {
                // 비밀번호 생성 실패 시 로그 출력
                throw new IllegalArgumentException("새 비밀번호 생성에 실패했습니다.");
            }
        } catch (Exception e) {
            // 예외 발생 시 처리 (예: 로그 기록)
            throw new IllegalArgumentException("비밀번호 생성 및 전송 중 오류 발생: " + e.getMessage());
        }
    }

    public String createNewPassword() {

        char[] chars = new char[]{
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
                'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
                'u', 'v', 'w', 'x', 'y', 'z',
                'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
                'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
                'U', 'V', 'W', 'X', 'Y', 'Z'
        };
        StringBuffer stringBuffer = new StringBuffer();
        SecureRandom securerandom = new SecureRandom();
        securerandom.setSeed(new Date().getTime());

        int index = 0;
        int length = chars.length;
        for(int i = 0; i < 8; i++){
            index = securerandom.nextInt(length);
        if(index % 2 == 0)
            stringBuffer.append(String.valueOf(chars[index]).toUpperCase());
        else
            stringBuffer.append(String.valueOf(chars[index]).toLowerCase());
        }
        return stringBuffer.toString();
    }

    public void sendNewPasswordByMail(String toMailAddr,String authentication) {
        Member member = memberRepository.findByMail(toMailAddr)
                .orElseThrow(() -> new IllegalArgumentException("member is not matching"));
        member.setAuthentication(authentication);

        final MimeMessagePreparator mimeMessagePreparator = new MimeMessagePreparator() {
            public void prepare(MimeMessage mimeMessage) throws Exception {

                final MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
                mimeMessageHelper.setTo(toMailAddr);
                mimeMessageHelper.setSubject("[AnimalPing] 인증번호 안내입니다.");
                mimeMessageHelper.setText("인증번호 " + authentication, true);

            };

        };javaMailSenderImpl.send(mimeMessagePreparator);

    }

    public void verifyNumber(VerifyMailDTO verifyMailDTO) {
        // 1. DB에서 이메일에 해당하는 인증값을 가져오기
        String storedAuthentication = memberRepository.findAuthentication(verifyMailDTO.getMail()); // 이메일을 파라미터로 사용

        // 2. 인증번호 비교
        if (storedAuthentication != null && storedAuthentication.equals(verifyMailDTO.getAuthentication())) {
            // 인증번호가 일치하면 인증 성공 처리
            log.info("인증번호 일치: 인증 성공");
        } else {
            // 인증번호가 일치하지 않으면 오류 처리
            log.warn("인증번호 불일치: 인증 실패");
            throw new IllegalArgumentException("인증번호가 일치하지 않습니다.");
        }
    }

@Transactional
public void changePassword(ChangePasswordDTO changePasswordDTO) {
    // 1. 이메일로 해당 사용자의 정보를 가져오기
    Optional<Member> memberOpt = memberRepository.findByMail(changePasswordDTO.getMail());

    // 2. 사용자가 존재하는지 확인
    if (memberOpt.isEmpty()) {
        throw new IllegalArgumentException("이메일에 해당하는 사용자가 없습니다.");
    }

    Member member = memberOpt.get();

    try {
        // 3. 비밀번호가 일치하는지 확인
        if (!changePasswordDTO.getCheckPassword().equals(changePasswordDTO.getNewPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // 4. 비밀번호 업데이트
        member.updatePassword(passwordEncoder, changePasswordDTO.getNewPassword());

        // 5. 업데이트된 회원 정보 저장
        memberRepository.save(member);

    } catch (IllegalArgumentException e) {
        log.warn("비밀번호 변경 실패: {}", e.getMessage());
        throw e; // 예외를 다시 던져서 호출자에게 알림
    }
}

    public void createKakao(Map<String, Object> userInfo) {

        String username = (String)userInfo.get("nickname") + "-" + System.currentTimeMillis();
        //동일 id 검사
        if (memberRepository.existsByUsername(username)) {
            log.warn("Username already exists {}", username);
            throw new IllegalArgumentException("user name already Exists");
        }

        //동일 메일 검사
        if (memberRepository.existsByMail((String)userInfo.get("email"))) {
            log.warn("Mail already exists {}", (String)userInfo.get("email"));
            throw new IllegalArgumentException("change kakao email");
        }
        String nickname = "";
        //동일 닉네임 검사
        while(true){
            String temp = globalService.getRandomNickname();
            if (!memberRepository.existsByNickname(temp)) {
                nickname =temp;
                break;
            }
        }

        Member member = Member.builder()
                .username(username)
                .password(passwordEncoder.encode(username))
                .mail((String)userInfo.get("email"))
                .nickname(nickname)
                .profile((String)userInfo.get("thumbnail"))
                .role(Role.USER)
                .build();

        memberRepository.save(member);
    }


    public MapPositionDTOResponse likePlace(String token, int page) {

        String userId = tokenProvider.extractIdByAccessToken(token);
        if(userId == null){
            throw new IllegalArgumentException("user is not found");
        }

        Pageable pageable = PageRequest.of(page, 15);
        Specification<MapEntity> specification = Specification.where(null);

        List<MapPositionDTO> mapPositionDTOList = new ArrayList<>();

        Page<MapLike> mapLikes = mapLikeRepository.findByMemberId(Long.valueOf(userId),pageable);

        for(MapLike mapLike : mapLikes){
            mapPositionDTOList.add(new MapPositionDTO(mapLike.getMap()));
        }

        return MapPositionDTOResponse.builder()
                .mapPositionDTOList(mapPositionDTOList)
                .total_count(mapLikes.getTotalElements())
                .build();

    }

    public PostResponseDTO myPost(String token, int page) {
        String temp = tokenProvider.extractIdByAccessToken(token);
        Long userId = Long.valueOf(temp);
        if(userId == null){
            throw new IllegalArgumentException("user is not found");
        }

        Pageable pageable = PageRequest.of(page, 15);

        Page<Post> posts = postRepository.findByMemberId(userId,pageable);

        List<PostListDTO> postListDTOList = posts.stream().map(PostListDTO::new).toList();

        return PostResponseDTO.builder()
                .posts(postListDTOList)
                .totalCount(posts.getTotalElements())
                .build();
    }

    public CommentResponseDTO myComment(String token, int page) {
        String temp = tokenProvider.extractIdByAccessToken(token);
        Long userId = Long.valueOf(temp);
        if(userId == null){
            throw new IllegalArgumentException("user is not found");
        }

        Pageable pageable = PageRequest.of(page, 15);

        Page<Comment> comments = commentRepository.findByMemberId(userId,pageable);

        List<CommentDTO> commentDTOS = new ArrayList<>();
        for(Comment comment : comments){
            CommentDTO commentDTO = new CommentDTO(comment);
            CommentHeart commentheart = commentHeartRepository.findByMemberIdAndCommentId(comment.getId(), userId);
            commentDTO.setHeart(commentheart!=null);
            commentDTOS.add(commentDTO);
        }

        return CommentResponseDTO
                .builder()
                .comments(commentDTOS)
                .totalCommentCount(comments.getTotalElements())
                .build();
    }

    public QueryResponse myQuery(String token, int page) {
        String temp = tokenProvider.extractIdByAccessToken(token);
        Long userId = Long.valueOf(temp);
        if(userId == null){
            throw new IllegalArgumentException("user is not found");
        }

        Pageable pageable = PageRequest.of(page, 15);

        Page<ItemQuery> itemQueries = itemQueryRepository.findByCustomerId(userId,pageable);

        // 4. DTO 변환
        List<ResponseItemQueryDTO> responseItemQueryDTOList = itemQueries
                .getContent()
                .stream()
                .map(ResponseItemQueryDTO::new)
                .toList();
        // 5. QueryResponse 생성 및 반환
        return QueryResponse.builder()
                .responseItemQueryDTOList(responseItemQueryDTOList)
                .total_count(itemQueries.getTotalElements())
                .build();
    }

    public ItemCommentDTOResponse myReview(String token, int page) {
        String temp = tokenProvider.extractIdByAccessToken(token);
        Long userId = Long.valueOf(temp);
        List<ItemCommentDTO> commentDTOS = new ArrayList<>();

        if(userId == null){
            throw new IllegalArgumentException("user is not found");
        }

        Pageable pageable = PageRequest.of(page, 15);

        Page<ItemComment> itemComments = itemCommentRepository.findByMemberId(userId,pageable);


        for(ItemComment comment : itemComments){
            ItemCommentDTO commentDTO = new ItemCommentDTO(comment);
            ItemCommentLike commentLike = itemCommentLikeRepository.findByItemCommentIdAndMemberId(comment.getId(), Long.valueOf(userId));
            commentDTO.setHeart(commentLike!=null);
            commentDTOS.add(commentDTO);
        }

        return ItemCommentDTOResponse
                .builder()
                .comments(commentDTOS)
                .total_count(itemComments.getTotalElements())
                .build();
    }

    public PostResponseDTO likePost(String token, int page) {
        String temp = tokenProvider.extractIdByAccessToken(token);
        Long userId = Long.valueOf(temp);
        if(userId == null){
            throw new IllegalArgumentException("user is not found");
        }

        Pageable pageable = PageRequest.of(page, 15);

        Page<Heart> hearts = heartRepository.findByMemberId(userId,pageable);
        List<Post> posts = new ArrayList<>();
        for(Heart heart : hearts){
            posts.add(heart.getPost());
        }

        List<PostListDTO> postListDTOList = posts.stream().map(PostListDTO::new).toList();

        return PostResponseDTO.builder()
                .posts(postListDTOList)
                .totalCount(hearts.getTotalElements())
                .build();
    }

    public CommentResponseDTO likeComment(String token, int page) {
        String temp = tokenProvider.extractIdByAccessToken(token);
        Long userId = Long.valueOf(temp);
        if(userId == null){
            throw new IllegalArgumentException("user is not found");
        }

        Pageable pageable = PageRequest.of(page, 15);

        Page<CommentHeart> commentHearts = commentHeartRepository.findByMemberId(userId, pageable);
        List<Comment> comments = new ArrayList<>();
        for(CommentHeart commentHeart : commentHearts){
            comments.add(commentHeart.getComment());
        }

        List<CommentDTO> commentDTOS = comments.stream().map(CommentDTO::new).toList();

        return CommentResponseDTO.builder()
                .comments(commentDTOS)
                .totalCommentCount(commentHearts.getTotalElements())
                .build();

    }

    public ItemCommentDTOResponse likeReview(String token, int page) {
        String temp = tokenProvider.extractIdByAccessToken(token);
        Long userId = Long.valueOf(temp);

        if(userId == null){
            throw new IllegalArgumentException("user is not found");
        }

        Pageable pageable = PageRequest.of(page, 15);

        Page<ItemCommentLike> itemCommentLikes = itemCommentLikeRepository.findByMemberId(userId,pageable);

        List<ItemComment> itemComments = new ArrayList<>();
        for(ItemCommentLike commentLike : itemCommentLikes){
            itemComments.add(commentLike.getItemComment());
        }

        List<ItemCommentDTO> responseItemQueryDTOList = itemComments.stream().map(ItemCommentDTO::new).toList();

        return ItemCommentDTOResponse
                .builder()
                .comments(responseItemQueryDTOList)
                .total_count(itemCommentLikes.getTotalElements())
                .build();
    }
}




