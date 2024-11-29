package animal_shop.community.member.service;

import animal_shop.community.member.Role;
import animal_shop.community.member.dto.*;
import animal_shop.community.member.entity.Member;
import animal_shop.community.member.entity.SellerCandidate;
import animal_shop.community.member.repository.MemberRepository;
import animal_shop.community.member.repository.SellerCandidateRepository;
import animal_shop.global.security.TokenProvider;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.Date;
import java.util.Optional;

@Service
@Slf4j
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private SellerCandidateRepository sellerCandidateRepository;

    @Autowired
    private JavaMailSenderImpl javaMailSenderImpl;



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
                .role(Role.USER)
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
        System.out.println("Member Service modify");

        Long userId = Long.valueOf(tokenProvider.extractIdByAccessToken(token));
        Optional<Member> member = memberRepository.findById(userId);

        if (memberRepository.existsByMail(memberDTO.getMail())) {
            log.warn("Mail already exists {}", memberDTO.getMail());
            throw new RuntimeException("mail already exists");
        }
        if (memberRepository.existsByNickname(memberDTO.getNickname())) {
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
    public MemberDTO getByToken(String token) {
        System.out.println("Member Service getByToken");
        Long userId = Long.valueOf(tokenProvider.extractIdByAccessToken(token));
        System.out.println(userId);
        Optional<Member> member = memberRepository.findById(userId);
        if (member.isPresent()) {
            return MemberDTO.builder()
                    .id(member.get().getId())
                    .username(member.get().getUsername())
                    .mail(member.get().getMail())
                    .nickname(member.get().getNickname())
                    .role(member.get().getRole())
                    .build();
        }
        return null;
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
    @Transactional
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
//    @Transactional
//    public void changePassword( ChangePasswordDTO changePasswordDTO) {
//        // 1. 이메일로 해당 사용자의 정보를 가져오기
//        Optional<Member> memberOpt = memberRepository.findByMail(changePasswordDTO.getMail());
//
//        // 2. 사용자가 존재하는지 확인
//        if (memberOpt.isEmpty()) {
//            throw new IllegalArgumentException("이메일에 해당하는 사용자가 없습니다.");
//        }
//
//        Member member = memberOpt.get();
//        if(changePasswordDTO.getCheckPassword().equals( changePasswordDTO.getNewPassword())){
//            member.updatePassword(passwordEncoder, changePasswordDTO.getNewPassword());
//        }else{
//            log.warn("비밀번호가 일치하지 않습니다.");
//        }
//
//        // 5. 업데이트된 회원 정보 저장
//        memberRepository.save(member);
//
//    }
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

}




