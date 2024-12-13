package animal_shop.global.security;

import animal_shop.community.member.dto.MemberDTO;
import animal_shop.community.member.entity.Member;
import animal_shop.community.member.repository.MemberRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Slf4j
@Service
public class  TokenProvider {

    @Value("${jwt.secret}")
    private String SECRET_KEY;

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

    public ArrayList<String> getKakaoTokenInfo(String accessToken){
        ArrayList<String > info = new ArrayList<>();
        try {
//            String accessToken = "WNH6hhhLlIsS2OCe_vkmn4diPsQvnbgyAAAAAQo9c5sAAAGTv6nmPtQ0RDl69jWm"; // 여기에 실제 엑세스 토큰을 입력하세요
            String urlString = "https://kapi.kakao.com/v2/user/me";

            // URL 객체 생성
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // HTTP 메소드 설정
            connection.setRequestMethod("GET");

            // Authorization 헤더 설정
            connection.setRequestProperty("Authorization", "Bearer " + accessToken);

            // 응답 코드 확인
            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) { // 200번 코드 확인
                // 응답을 읽어오기 위한 스트림 설정
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                // 응답 내용 읽기
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // JSON 데이터 파싱 및 추출
                String responseBody = response.toString();
                JSONObject jsonResponse = new JSONObject(responseBody);

                // nickname 추출
                JSONObject properties = jsonResponse.getJSONObject("properties");
                String nickname = properties.getString("nickname");

                // thumbnail_image 추출
                String thumbnailImage = properties.getString("thumbnail_image");

                // email 추출
                JSONObject kakaoAccount = jsonResponse.getJSONObject("kakao_account");
                String email = kakaoAccount.getString("email");
                // 결과 출력
                log.info("kakao token extract data nickname = {}, thumbnail = {}, email = {}",nickname,thumbnailImage,email);
                info.add(nickname);
                info.add(thumbnailImage);
                info.add(email);

            } else {
                System.out.println("Error: Failed to get response from Kakao API.");
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return info;
    }
}
