package animal_shop.community.member.controller;

import animal_shop.community.member.dto.*;
import animal_shop.community.member.service.MemberService;
import animal_shop.global.dto.ResponseDTO;
import animal_shop.global.security.TokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/auth")
public class MemberController {

    @Autowired
    private MemberService memberService;

    @Autowired
    private TokenProvider tokenProvider;

    @PostMapping(value = "/signup")
    public ResponseEntity<ResponseDTO> registerMember(@RequestBody MemberDTO memberDTO) {
        ResponseDTO responseDTO;
        String message;
        try {
            int result = memberService.create(memberDTO);
            // result 값을 기준으로 응답 메시지 결정
            switch (result) {
                case 0:
                    message = "SignUp success";
                    break;
                case 1:
                    message = "Username already exists";
                    break;
                case 2:
                    message = "Email already exists";
                    break;
                case 3:
                    message = "Nickname already exists";
                    break;
                default:
                    message = "Sign Up Failed";
                    break;
            }

            // 응답 DTO 빌드 및 반환
            responseDTO = ResponseDTO.builder()
                    .message(message)
                    .build();

            if (result == 0) {
                return ResponseEntity.ok(responseDTO);
            } else {
                return ResponseEntity.badRequest().body(responseDTO);
            }

        } catch (Exception e) {
            // 예외 상황 처리 및 로그 기록
            log.error("Signup failed for user: {}, error: {}", memberDTO.getUsername(), e.getMessage());

            responseDTO = ResponseDTO.builder()
                    .error("Signup failed.")
                    .build();

            return ResponseEntity
                    .badRequest()
                    .body(responseDTO);
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signin(@RequestBody MemberDTO memberDTO) {
        ResponseDTO responseDTO;
        String message = "";
        try {
            MemberDTO mDTO = memberService.getByCredentials(memberDTO);

            if (mDTO != null) {
                final TokenDTO tokenDTO = memberService.login(mDTO);
                return ResponseEntity.ok().body(tokenDTO);
            } else {
                throw new IllegalArgumentException("id password wrong");
            }
        } catch (Exception e) {
            log.error("login failed for user: {}, error: {}", memberDTO.getUsername(), e.getMessage());

            responseDTO = ResponseDTO.builder()
                    .error(e.getMessage())
                    .build();

            return ResponseEntity
                    .badRequest()
                    .body(responseDTO);
        }
    }


    @DeleteMapping("/delete")
    public ResponseEntity<?> memberDelete(@RequestHeader("Authorization") String token) {
        ResponseDTO responseDTO;
        try {
            memberService.delete(token);
            responseDTO = ResponseDTO.builder().message("delete success").build();
            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            responseDTO = ResponseDTO.builder()
                    .error(e.getMessage()).build();
            return ResponseEntity
                    .badRequest()
                    .body(responseDTO);
        }
    }

    @PostMapping("/token")
    public ResponseEntity<?> createNewAccessToken(@RequestBody TokenDTO tokenDTO) {
        try {
            TokenDTO token = memberService.getNewAccessToken(tokenDTO);
            return ResponseEntity.ok().body(token);

        } catch (Exception e) {
            System.out.println("error processing");
            ResponseDTO responseDTO = ResponseDTO.builder()
                    .error("refreshToken expired")
                    .build();

            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @PostMapping("/findPassword")
    public ResponseEntity<?> findPassword(@RequestBody SendMailDTO sendMailDTO) {
        ResponseDTO responseDTO = null;
        try {
            memberService.createAndSendNewPassword(sendMailDTO);
            responseDTO = ResponseDTO.builder()
                    .message("success sending mail")
                    .build();
            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            responseDTO = ResponseDTO.builder()
                    .message(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }
    @PostMapping("/verify")
    public ResponseEntity<?> verifyNumber(@RequestBody VerifyMailDTO verifyMailDTO) {

        ResponseDTO responseDTO = null;
        try {
            memberService.verifyNumber(verifyMailDTO); // 인증번호와 이메일을 넘겨줌
            responseDTO = ResponseDTO.builder()
                    .message("success verify")
                    .build();
            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            responseDTO = ResponseDTO.builder()
                    .message(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }
    @PatchMapping("/changePassword")
    public ResponseEntity<?> changPassword(@RequestBody ChangePasswordDTO changePasswordDTO){
        ResponseDTO responseDTO = null;
        try {
            //해당 이메일에 비밀번호를 바꿀려고 하는거니까
            memberService.changePassword(changePasswordDTO);
            responseDTO = ResponseDTO.builder()
                    .message("success change")
                    .build();
            return ResponseEntity.ok().body(responseDTO);
        }catch (Exception e){
            responseDTO = ResponseDTO.builder()
                    .message(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
   }
    @PostMapping("/kakao/signup")
    public ResponseEntity<?> kakaoSignup(@RequestBody KakaoDTO kakaoDTO) {
        ResponseDTO responseDTO;
        String message = "";
        try {
            // 1. 인가 코드 받기 (@RequestParam String code)

            // 2. 토큰 받기
            String accessToken = tokenProvider.getKakaoAccessToken(kakaoDTO.getCode());

            // 3. 사용자 정보 받기
            Map<String, Object> userInfo = tokenProvider.getUserInfoFromKakao(accessToken);

            String email = (String)userInfo.get("email");
            String nickname = (String)userInfo.get("nickname");
            memberService.createKakao(userInfo);

            responseDTO = ResponseDTO.builder()
                    .message("signup success")
                    .build();

            return ResponseEntity
                    .ok().body(responseDTO);
        } catch (Exception e) {

            responseDTO = ResponseDTO.builder()
                    .error(e.getMessage())
                    .build();

            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @PostMapping("/kakao/signin")
    public ResponseEntity<?> kakaoSignin(@RequestBody KakaoDTO kakaoDTO) {
        ResponseDTO responseDTO;
        String message = "";
        try {
            // 1. 인가 코드 받기 (@RequestParam String code)

            // 2. 토큰 받기
            String accessToken = tokenProvider.getKakaoAccessToken(kakaoDTO.getCode());

            // 3. 사용자 정보 받기
            Map<String, Object> userInfo = tokenProvider.getUserInfoFromKakao(accessToken);

            String email = (String)userInfo.get("email");
            String nickname = (String)userInfo.get("nickname");
            MemberDTO memberDTO = memberService.getBymail(email);

            TokenDTO tokenDTO = memberService.login(memberDTO);

            return ResponseEntity.ok().body(tokenDTO);
        } catch (Exception e) {
            responseDTO = ResponseDTO.builder()
                    .error(e.getMessage())
                    .build();

            return ResponseEntity
                    .badRequest()
                    .body(responseDTO);
        }
    }
    @PostMapping("seller/signup")
    public ResponseEntity<?>sellerSignup(@RequestBody SellerSignUpDTO sellerSignUpDTO){
        ResponseDTO responseDTO = null;
        String message;
        try{
            int result = memberService.createSeller(sellerSignUpDTO);
            switch (result) {
                case 0:
                    message = "SignUp success";
                    break;
                case 1:
                    message = "Username already exists";
                    break;
                case 2:
                    message = "Email already exists";
                    break;
                case 3:
                    message = "Nickname already exists";
                    break;
                case 4:
                    message = "PhoneNumber already exists";
                    break;
                case 5:
                    message = "bln already exists";
                    break;
                default:
                    message = "Sign Up Failed";
                    break;
            }
            responseDTO = ResponseDTO.builder()
                    .message("Doge")
                    .build();
            if(result == 0){
                return ResponseEntity.ok().body(responseDTO);
            }else {
                return ResponseEntity.badRequest().body(responseDTO);
            }
        }catch (Exception e){
            log.error("Signup failed for user: {}, error: {}", sellerSignUpDTO.getUsername(), e.getMessage());
            responseDTO = ResponseDTO.builder()
                    .error("SignUp failed")
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

}
