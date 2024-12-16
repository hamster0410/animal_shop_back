package animal_shop.global.naver.controller;

import animal_shop.global.naver.dto.NaverpaymentDTO;
import animal_shop.global.naver.service.NaverpayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")

public class NaverpayController {

    @Autowired
    private NaverpayService payService;

    @PostMapping("/create-order")
    public ResponseEntity<String> createOrder(@RequestBody NaverpaymentDTO paymentDTO) {
        String orderId = payService.createOrder(paymentDTO);
        return ResponseEntity.ok(orderId);
    }

    @PostMapping("/approve")
    public ResponseEntity<String> approvePayment(@RequestParam String paymentId) {
        String result = payService.approvePayment(paymentId);
        return ResponseEntity.ok(result);
    }
}


