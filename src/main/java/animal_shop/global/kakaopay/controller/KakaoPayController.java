package animal_shop.global.kakaopay.controller;

import animal_shop.global.kakaopay.dto.KakaoApproveResponse;
import animal_shop.global.kakaopay.dto.KakaoCancelRequest;
import animal_shop.global.kakaopay.dto.KakaoCancelResponse;
import animal_shop.global.kakaopay.dto.KakaoReadyRequest;
import animal_shop.global.kakaopay.service.KakaoPayService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pay")
@RequiredArgsConstructor
public class KakaoPayController {

    private final KakaoPayService kakaoPayService;

    /**
     * 결제요청
     */
    @PostMapping("/kakaoReady")
    public ResponseEntity readyToKakaoPay(@RequestBody KakaoReadyRequest kakaoReadyRequest) {
        return ResponseEntity.ok().body(kakaoPayService.kakaoPayReady(kakaoReadyRequest));
    }
    /**
     * 결제 성공
     */
    @GetMapping("/success")
    public ResponseEntity afterPayRequest(@RequestParam("tid") String tid,@RequestParam("pg_token") String pgToken) {

        KakaoApproveResponse kakaoApprove = kakaoPayService.approveResponse(tid, pgToken);

        return new ResponseEntity<>(kakaoApprove, HttpStatus.OK);
    }
    /**
     * 결제 진행 중 취소
     */
    @GetMapping("/kakaoCancel")
    public void cancel() {


        throw new IllegalArgumentException("pay cancel");
    }

    /**
     * 결제 실패
     */
    @GetMapping("/kakaoFail")
    public void fail() {

        throw new IllegalArgumentException("pay fail");
    }

    /**
     * 환불
     */
    @PostMapping("/refund")
    public ResponseEntity refund(@RequestBody KakaoCancelRequest kakaoCancelRequest) {

        KakaoCancelResponse kakaoCancelResponse = kakaoPayService.kakaoCancel(kakaoCancelRequest);

        return new ResponseEntity<>(kakaoCancelResponse, HttpStatus.OK);
    }
}