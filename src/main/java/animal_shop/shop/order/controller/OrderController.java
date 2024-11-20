package animal_shop.shop.order.controller;

import animal_shop.global.dto.ResponseDTO;
import animal_shop.shop.order.dto.OrderDTO;
import animal_shop.shop.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/shop")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping(value = "/order")
    public ResponseEntity<?> order(@RequestHeader(value = "Authorization") String token,
                                   @RequestBody OrderDTO orderDTO){
        ResponseDTO responseDTO;
        try{
            Long orderId = orderService.order(orderDTO,token);
            return ResponseEntity.ok().body(orderId);
        }catch (Exception e){
            responseDTO = ResponseDTO.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }
}
