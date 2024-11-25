package animal_shop.global.kakaopay.service;

import animal_shop.global.kakaopay.dto.KakaoApproveResponse;
import animal_shop.global.kakaopay.dto.KakaoCancelResponse;
import animal_shop.global.kakaopay.dto.KakaoReadyRequest;
import animal_shop.global.kakaopay.dto.KakaoReadyResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

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

    private KakaoReadyResponse kakaoReady;
    public KakaoReadyResponse kakaoPayReady(KakaoReadyRequest kakaoReadyRequest) {

        // 카카오페이 요청 양식
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("cid", cid);
        parameters.add("partner_order_id", kakaoReadyRequest.getPartner_order_id());
        parameters.add("partner_user_id", kakaoReadyRequest.getPartner_user_id());
        parameters.add("item_name", kakaoReadyRequest.getItem_name());
        parameters.add("quantity", kakaoReadyRequest.getQuantity());
        parameters.add("total_amount", kakaoReadyRequest.getTotal_amount());
        parameters.add("vat_amount", kakaoReadyRequest.getVat_amount());
        parameters.add("tax_free_amount", kakaoReadyRequest.getTax_free_amount());
        parameters.add("approval_url", server_address + "/pay/success"); // 성공 시 redirect url
        parameters.add("cancel_url", server_address + "/pay/kakaoCancel"); // 취소 시 redirect url
        parameters.add("fail_url", server_address + "/pay/kakaoFail"); // 실패 시 redirect url

        // 파라미터, 헤더
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());

        // 외부에 보낼 url
        RestTemplate restTemplate = new RestTemplate();

        kakaoReady = restTemplate.postForObject(
                "https://kapi.kakao.com/v1/payment/ready",
                requestEntity,
                KakaoReadyResponse.class);

        return kakaoReady;
    }
    /**
     * 결제 완료 승인
     */
    public KakaoApproveResponse approveResponse(String pgToken) {

        // 카카오 요청
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("cid", cid);
        parameters.add("tid", kakaoReady.getTid());
        parameters.add("partner_order_id", "가맹점 주문 번호");
        parameters.add("partner_user_id", "가맹점 회원 ID");
        parameters.add("pg_token", pgToken);

        // 파라미터, 헤더
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());

        // 외부에 보낼 url
        RestTemplate restTemplate = new RestTemplate();

        KakaoApproveResponse approveResponse = restTemplate.postForObject(
                "https://kapi.kakao.com/v1/payment/approve",
                requestEntity,
                KakaoApproveResponse.class);

        return approveResponse;
    }

    /**
     * 결제 환불
     */
    public KakaoCancelResponse kakaoCancel() {

        // 카카오페이 요청
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("cid", cid);
        parameters.add("tid", "환불할 결제 고유 번호");
        parameters.add("cancel_amount", "환불 금액");
        parameters.add("cancel_tax_free_amount", "환불 비과세 금액");
        parameters.add("cancel_vat_amount", "환불 부가세");

        // 파라미터, 헤더
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());

        // 외부에 보낼 url
        RestTemplate restTemplate = new RestTemplate();

        KakaoCancelResponse cancelResponse = restTemplate.postForObject(
                "https://kapi.kakao.com/v1/payment/cancel",
                requestEntity,
                KakaoCancelResponse.class);

        return cancelResponse;
    }

    private HttpHeaders getHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();

        String auth = "KakaoAK " + admin_Key;

        httpHeaders.set("Authorization", auth);
        httpHeaders.set("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        return httpHeaders;
    }

}
