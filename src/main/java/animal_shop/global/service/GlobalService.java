package animal_shop.global.service;

import animal_shop.community.member.entity.Member;
import animal_shop.community.member.repository.MemberRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class GlobalService {

    @Autowired
    MemberRepository memberRepository;

    public String getNickname(Long userId){
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("member is not found"));

        return member.getNickname();
    }

    public String getProfile(Long userId){
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("member is not found"));
        if(member.getProfile() == null){
            return null;
        }
        return member.getProfile();
    }

    public String getRandomNickname(){
        try {
            String url = "https://www.rivestsoft.com/nickname/getRandomNickname.ajax";
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            // 요청 메소드 설정
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");

            // 요청 본문 설정
            String jsonBody = "{\"lang\": \"ko\"}";
            con.setDoOutput(true);
            try (OutputStream os = con.getOutputStream()) {
                byte[] input = jsonBody.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            // 응답 코드 확인
            int responseCode = con.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                // 응답 데이터 읽기
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // JSON 파싱
                JSONObject jsonResponse = new JSONObject(response.toString());
                return jsonResponse.getString("data");
            } else {
                throw new IllegalStateException("요청 실패");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void getTestCode() {
        try {
            String accessToken = "WNH6hhhLlIsS2OCe_vkmn4diPsQvnbgyAAAAAQo9c5sAAAGTv6nmPtQ0RDl69jWm"; // 여기에 실제 엑세스 토큰을 입력하세요
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
            System.out.println("Response Code: " + responseCode);

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
                System.out.println("Nickname: " + nickname);
                System.out.println("Thumbnail Image: " + thumbnailImage);
                System.out.println("Email: " + email);
            } else {
                System.out.println("Error: Failed to get response from Kakao API.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
