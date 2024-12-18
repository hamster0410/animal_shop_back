package animal_shop.global.pay.service;

import animal_shop.global.pay.dto.NaverpaymentDTO;
import animal_shop.global.pay.entity.Naverpayment;
import animal_shop.global.pay.repository.NaverpaymentRepository;
import com.fasterxml.jackson.databind.JsonNode;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NaverpayService {


    @Autowired
    NaverpaymentRepository paymentRepository;


    @Value("${naver.pay.animalping-id}")
    private String animalpingNpayId;


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
        String url = "https://sandbox-api.naver.com/v1/payments/request" ;

        // 요청 본문 생성
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Naver-Client-Id", clientId);
        headers.set("X-Naver-Client-Secret", clientSecret);
        headers.set("Content-Type","application/json");

        Map<String, Object> payParams = new HashMap<String, Object>();
        payParams.put("merchantPayKey",paymentDTO.getMerchantPayKey());
        payParams.put("merchantUserKey",paymentDTO.getMerchantUserKey());
        payParams.put("productName",paymentDTO.getProductName());
        payParams.put("productCount",paymentDTO.getProductCount());
        payParams.put("totalPayAmount",paymentDTO.getTotalPayAmount());
        payParams.put("taxScopeAmount",paymentDTO.getTaxScopeAmount());
        payParams.put("taxExScopeAmount",paymentDTO.getTaxExScopeAmount());
        payParams.put("returnUrl",paymentDTO.getReturnUrl());

        //상품 관련 정보
        List<Map<String, Object>> items = new ArrayList<Map<String,Object>>();
        Map<String, Object> item = new HashMap<String,Object>();
        item.put("categoryType" , "PRODUCT");
        item.put("categoryId" , "GENERAL");
        item.put("uid" , "product");
        item.put("name" , paymentDTO.getProductName());
        item.put("count",paymentDTO.getProductCount());
        items.add(item);

        payParams.put("productItems", items);
        //해시맵을 json데이터로 전환
        JSONObject jObj = new JSONObject(payParams);
        HttpEntity<?> request = new HttpEntity<>(jObj.toString(), headers);

        RestTemplate restTemplate = new RestTemplate();

        Map<String,Object> res = restTemplate.postForObject(url, request, Map.class);
        System.out.println(res);
        return null;



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
            RestTemplate restTemplate = new RestTemplate();

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
