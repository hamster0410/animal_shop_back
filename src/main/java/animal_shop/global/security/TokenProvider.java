package animal_shop.global.security;

import animal_shop.community.member.dto.MemberDTO;
import animal_shop.community.member.dto.TokenDTO;
import animal_shop.community.member.entity.Member;
import animal_shop.community.member.repository.MemberRepository;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Slf4j
@Service
public class  TokenProvider {

    @Value("${jwt.secret}")
    private String SECRET_KEY;

    @Value("${kakao.oauth_key}")
    private String OauthKey;

    @Value("${kakao.token_api}")
    private String kakaoGetTokenApi;

    @Value("${kakao.get_user_info}")
    private String kakaoGetUserInfoApi;

    @Autowired
    MemberRepository memberRepository;

    public String AccessTokenCreate(String memberId) {
        Date expiryDate = Date.from(
                Instant.now()
                        .plus(15, ChronoUnit.HOURS)
        );
        Long id = Long.valueOf(memberId);
        Member member = memberRepository.findById(id).orElseThrow();
        // JWT Claims에 role 정보를 추가
        Map<String, Object> claims = new HashMap<>();

        claims.put("role", member.getRole());  // 예를 들어 role이 "USER" 또는 "ADMIN" 등
        claims.put("profileImg",member.getProfile());
        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .setSubject(memberId)
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .compact();
    }
    public String RefreshTokenCreate(MemberDTO member) {
        Date expiryDate = Date.from(
                Instant.now()
                        .plus(15, ChronoUnit.HOURS)
        );


        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .setSubject(member.getId().toString())
                .setIssuer("demo app")
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .compact();
    }

    public String validateAndGetUserId(String token){
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .build().parseClaimsJws(token)
                .getBody();
        if (claims.getExpiration().before(new Date())) {
            throw new ExpiredJwtException(null, claims, "Token has expired");
        }
        return claims.getSubject();
    }

    public String extractIdByAccessToken(String accessToken){
        System.out.println("[TokenProvider] extractAllClaims");
        String jwt = accessToken.replace("Bearer ", "").trim(); // 'Bearer ' 부분 제거 및 공백 제거

        // Claims 객체 가져오기
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                        .build().parseClaimsJws(jwt)
                        .getBody();
        return claims.getSubject();
    }


    public String extractIdByRefreshToken(String refreshToken){

        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .build().parseClaimsJws(refreshToken)
                .getBody().getSubject();
    }

    public String getRoleFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .build().parseClaimsJws(token)
                .getBody();
        return claims.get("role", String.class);
    }

    public Map<String, Object> getUserInfoFromKakao(String accessToken) {
        HashMap<String, Object> userInfo = new HashMap<>();
        String reqUrl = kakaoGetUserInfoApi;
        try{
            URL url = new URL(reqUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Bearer " + accessToken);
            conn.setRequestProperty("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

            int responseCode = conn.getResponseCode();
            log.info("[KakaoApi.getUserInfo] responseCode : {}",  responseCode);

            BufferedReader br;
            if (responseCode >= 200 && responseCode <= 300) {
                br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }

            String line = "";
            StringBuilder responseSb = new StringBuilder();
            while((line = br.readLine()) != null){
                responseSb.append(line);
            }
            String result = responseSb.toString();
            log.info("responseBody = {}", result);

            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);

            JsonObject properties = element.getAsJsonObject().get("properties").getAsJsonObject();
            JsonObject kakaoAccount = element.getAsJsonObject().get("kakao_account").getAsJsonObject();

            String nickname = properties.getAsJsonObject().get("nickname").getAsString();
            String email = kakaoAccount.getAsJsonObject().get("email").getAsString();

            String thumbnail = properties.getAsJsonObject().get("thumbnail_image").getAsString();

            userInfo.put("nickname", nickname);
            userInfo.put("email", email);
            userInfo.put("thumbnail", thumbnail);

            br.close();

        }catch (Exception e){
            e.printStackTrace();
        }
        return userInfo;
    }

    public ResponseEntity<Void> sendToken(TokenDTO tokenDTO) {
        String targetUrl = "http://localhost:3000/login"; // 데이터를 보낼 엔드포인트
        RestTemplate restTemplate = new RestTemplate();

        try {
            // HTTP 헤더 설정
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // 요청 엔티티 생성
            HttpEntity<TokenDTO> requestEntity = new HttpEntity<>(tokenDTO, headers);

            // 1. 데이터를 targetUrl로 전송
            ResponseEntity<String> response = restTemplate.postForEntity(targetUrl, requestEntity, String.class);
            System.out.println("Response from target server: " + response.getBody());

            // 2. 리다이렉트 응답 반환
            return ResponseEntity
                    .status(HttpStatus.FOUND) // HTTP 302 FOUND
                    .location(URI.create("http://localhost:3000/")) // 리다이렉트할 주소
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
