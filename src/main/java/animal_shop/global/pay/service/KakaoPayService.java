package animal_shop.global.pay.service;

import animal_shop.community.member.entity.Member;
import animal_shop.community.member.repository.MemberRepository;
import animal_shop.global.pay.dto.*;
import animal_shop.global.pay.entity.KakaoPay;
import animal_shop.global.pay.repository.KakaoPayRepository;
import animal_shop.global.security.TokenProvider;
import animal_shop.shop.order.entity.Order;
import animal_shop.shop.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
public class KakaoPayService {

    @Value("${kakaopay.cid}")  // 파일 저장 경로를 application.properties에 설정
    private String cid;

    @Value("${kakaopay.admin_key}")
    private String admin_Key;

    @Value("${kakaopay.server}")
    private String server_address;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    KakaoPayRepository kakaoPayRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    TokenProvider tokenProvider;

    public KakaoReadyResponse kakaoPayReady(KakaoReadyRequest kakaoReadyRequest) {
        // 요청 URL
        String url = "https://open-api.kakaopay.com/online/v1/payment/ready";

        // 요청 바디 설정
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("cid", "TC0ONETIME"); // 테스트용 CID
        requestBody.put("partner_order_id", kakaoReadyRequest.getPartner_order_id());
        requestBody.put("partner_user_id", kakaoReadyRequest.getPartner_user_id());
        requestBody.put("item_name", kakaoReadyRequest.getItem_name());
        requestBody.put("quantity", kakaoReadyRequest.getQuantity());
        requestBody.put("total_amount", kakaoReadyRequest.getTotal_amount());
        requestBody.put("vat_amount", "0");
        requestBody.put("tax_free_amount", "0");
        requestBody.put("approval_url", server_address + "/pay/success");
        requestBody.put("fail_url", server_address + "/pay/cancel");
        requestBody.put("cancel_url", server_address + "/pay/fail");
        // HTTP 요청 생성
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, getHeaders());

        // RestTemplate 호출
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<KakaoReadyResponse> responseEntity;

        try {
            responseEntity = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    requestEntity,
                    KakaoReadyResponse.class
            );
            responseEntity.getBody().setPartner_order_id(kakaoReadyRequest.getPartner_order_id());
        } catch (Exception e) {
            //에러 시 주문 취소 
            throw new RuntimeException("Kakao API 호출 실패: " + e.getMessage(), e);
        }

        // 응답 반환
        return responseEntity.getBody();
    }
    /**
     * 결제 완료 승인
     */
    public KakaoApproveResponse approveResponse(KakaoSuccessRequest kakaoSuccessRequest,String token) {
        // 요청 URL
        String url = "https://open-api.kakaopay.com/online/v1/payment/approve";

        String userId = tokenProvider.extractIdByAccessToken(token);
        Member member = memberRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new IllegalArgumentException("member is not found"));
        // 요청 바디 설정
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("cid", "TC0ONETIME"); // 테스트용 CID
        requestBody.put("tid", kakaoSuccessRequest.getTid()); // 결제 고유 번호
        requestBody.put("partner_order_id", kakaoSuccessRequest.getPartner_order_id()); // 가맹점 주문 번호
        requestBody.put("partner_user_id", member.getNickname()); // 가맹점 회원 ID
        requestBody.put("pg_token", kakaoSuccessRequest.getPg_token()); // 결제승인 요청 토큰

         // HTTP 요청 생성
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, getHeaders());

        // RestTemplate 호출
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<KakaoApproveResponse> responseEntity;

        try {
            responseEntity = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    requestEntity,
                    KakaoApproveResponse.class
            );

            //주문 상태들 결제로 바꿈
            Order order = orderRepository.findByOrderCode(kakaoSuccessRequest.getPartner_order_id());
            order.paySuccess();
            //카카오페이 결제 정보를 db에 저장
            kakaoPayRepository.save(new KakaoPay(Objects.requireNonNull(responseEntity.getBody())));
        } catch (Exception e) {
            //에러 시 주문 취소
            throw new RuntimeException("Kakao API 호출 실패: " + e.getMessage(), e);
        }


        // 응답 반환
        return responseEntity.getBody();
    }

    /**
     * 결제 취소 요청
     */
    public KakaoCancelResponse kakaoCancel(KakaoCancelRequest kakaoCancelRequest) {
        // 요청 URL
        String url = "https://open-api.kakaopay.com/online/v1/payment/cancel";

        // 요청 바디 설정
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("cid", "TC0ONETIME"); // 테스트용 CID
        requestBody.put("tid", kakaoCancelRequest.getTid()); // 결제 고유 번호
        requestBody.put("cancel_amount", kakaoCancelRequest.getCancelAmount()); // 취소 요청 금액
        requestBody.put("cancel_tax_free_amount", kakaoCancelRequest.getCancelTaxFreeAmount()); // 취소 비과세 금액
        requestBody.put("cancel_vat_amount", kakaoCancelRequest.getCancelVatAmount()); // 취소 부가세 금액

        // HTTP 요청 생성
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, getHeaders());

        // RestTemplate 호출
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<KakaoCancelResponse> responseEntity;

        try {
            responseEntity = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    requestEntity,
                    KakaoCancelResponse.class
            );
            Objects.requireNonNull(responseEntity.getBody()).setItem_name(kakaoCancelRequest.getItemName());
            responseEntity.getBody().setQuantity(kakaoCancelRequest.getCancelAmount());
        } catch (Exception e) {
            throw new RuntimeException("Kakao API 호출 실패: " + e.getMessage(), e);
        }

        // 응답 반환
        return responseEntity.getBody();
    }
    private HttpHeaders getHeaders() {

        // 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "SECRET_KEY " + admin_Key);

        return headers;
    }

}
