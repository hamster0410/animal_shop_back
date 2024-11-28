package animal_shop.community.member.service;

import animal_shop.community.member.Role;
import animal_shop.community.member.dto.MemberDTO;
import animal_shop.community.member.dto.SellerRegisterDTO;
import animal_shop.community.member.dto.TokenDTO;
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
    public void createAndSendNewPassword(String toMailAddr) {
        try {
            // 새로운 비밀번호 생성
            String newPassword = createNewPassword();
//            member의 임시비밀번호 저장

            // 비밀번호가 생성되면 이메일로 전송
            if (newPassword != null && !newPassword.isEmpty()) {
                sendNewPasswordByMail(toMailAddr, newPassword);
            } else {
                // 비밀번호 생성 실패 시 로그 출력
                System.err.println("새 비밀번호 생성에 실패했습니다.");
            }
        } catch (Exception e) {
            // 예외 발생 시 처리 (예: 로그 기록)
            System.err.println("비밀번호 생성 및 전송 중 오류 발생: " + e.getMessage());
        }
    }

    public String createNewPassword() {
        System.out.println("here1");
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

    private void sendNewPasswordByMail(String toMailAddr,String newPassword) {
        final MimeMessagePreparator mimeMessagePreparator = new MimeMessagePreparator() {
            public void prepare(MimeMessage mimeMessage) throws Exception {
                System.out.println("here2");
                final MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
                mimeMessageHelper.setTo(toMailAddr);
                mimeMessageHelper.setSubject("[Space X] 새로운 비밀번호 안내입니다.");
                mimeMessageHelper.setText("새 비밀번호" + newPassword, true);
                System.out.println("here3");
            };

        };javaMailSenderImpl.send(mimeMessagePreparator);
        System.out.println("gg");
    }


}



