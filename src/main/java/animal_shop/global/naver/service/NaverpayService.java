package animal_shop.global.naver.service;

import animal_shop.global.naver.dto.NaverpaymentDTO;
import animal_shop.global.naver.entity.Naverpayment;
import animal_shop.global.naver.repository.NaverpaymentRepository;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
@Service
public class NaverpayService {


    @Autowired
    NaverpaymentRepository paymentRepository;

    @Autowired
    RestTemplate restTemplate;

    @Value("${naver.pay.client-id}")
    private String clientId;

    @Value("${naver.pay.client-secret}")
    private String clientSecret;

    @Value("${naver.pay.chain-id}")
    private String chainId;

    // 주문 생성 메서드
    @Transactional
    public String createOrder(NaverpaymentDTO paymentDTO) {
        // 네이버페이 API 요청 URL (샌드박스 환경)
        String url = "https://sandbox-api.naver.com/payments/v1/orders";

        // 요청 본문 생성
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Naver-Client-Id", clientId);
        headers.set("X-Naver-Client-Secret", clientSecret);

        // JSON 요청 데이터 작성
        String requestBody = """
            {
                "merchantPayKey": "%s",
                "productName": "%s",
                "productCount": %d,
                "totalPayAmount": %d,
                "taxScopeAmount": %d,
                "taxExScopeAmount": %d,
                "returnUrl": "%s"
            }
            """.formatted(
                paymentDTO.getMerchantPayKey(),
                paymentDTO.getProductName(),
                paymentDTO.getProductCount(),
                paymentDTO.getTotalPayAmount(),
                paymentDTO.getTaxScopeAmount(),
                paymentDTO.getTaxExScopeAmount(),
                paymentDTO.getReturnUrl()
        );

        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

        try {
            // 네이버페이 API 호출
            ResponseEntity<JsonNode> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, JsonNode.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                JsonNode responseBody = response.getBody();
                assert responseBody != null;

                // 주문 ID 추출
                String orderId = responseBody.get("orderId").asText();

                // 데이터베이스 저장
                Naverpayment payment = Naverpayment.builder()
                        .merchantPayKey(paymentDTO.getMerchantPayKey())
                        .productName(paymentDTO.getProductName())
                        .productCount(paymentDTO.getProductCount())
                        .totalPayAmount(paymentDTO.getTotalPayAmount())
                        .taxScopeAmount(paymentDTO.getTaxScopeAmount())
                        .taxExScopeAmount(paymentDTO.getTaxExScopeAmount())
                        .returnUrl(paymentDTO.getReturnUrl())
                        .build();

                paymentRepository.save(payment);

                return orderId; // 생성된 주문 ID 반환
            } else {
                throw new RuntimeException("네이버페이 주문 생성 실패: " + response.getStatusCode());
            }
        } catch (Exception e) {
            throw new RuntimeException("네이버페이 요청 중 에러 발생", e);
        }
    }
    // 결제 승인 메서드
    public String approvePayment(String paymentId) {
        // 네이버페이 API 요청 URL (샌드박스 환경)
        String url = "https://sandbox-api.naver.com/payments/v1/approval";

        // 요청 본문 생성
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Naver-Client-Id", clientId); // @Value에서 주입된 값
        headers.set("X-Naver-Client-Secret", clientSecret); // @Value에서 주입된 값

        // JSON 요청 데이터 생성
        String requestBody = """
        {
            "paymentId": "%s"
        }
        """.formatted(paymentId);

        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

        try {
            // 네이버페이 API 호출
            ResponseEntity<JsonNode> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, JsonNode.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                JsonNode responseBody = response.getBody();
                assert responseBody != null;

                // API 응답 결과 확인
                String result = responseBody.get("result").asText();

                if ("SUCCESS".equalsIgnoreCase(result)) {
                    // 결제 성공 시, DB에서 주문 상태 업데이트
                    Naverpayment payment = paymentRepository.findByMerchantPayKey(paymentId)
                            .orElseThrow(() -> new RuntimeException("해당 결제를 찾을 수 없습니다. Payment ID: " + paymentId));

                    payment.setStatus("PAID");
                    paymentRepository.save(payment);

                    return "결제가 성공적으로 완료되었습니다.";
                } else {
                    // 실패 이유 반환
                    String errorCode = responseBody.get("error").get("code").asText();
                    String errorMessage = responseBody.get("error").get("message").asText();
                    throw new RuntimeException("결제 승인 실패: " + errorCode + " - " + errorMessage);
                }
            } else {
                throw new RuntimeException("네이버페이 결제 승인 실패: HTTP 상태 코드 " + response.getStatusCode());
            }
        } catch (Exception e) {
            // 에러 발생 시 처리
            throw new RuntimeException("결제 승인 중 오류 발생: " + e.getMessage(), e);
        }
    }

}
