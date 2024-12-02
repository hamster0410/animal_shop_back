package animal_shop.shop.order.controller;

import animal_shop.global.dto.ResponseDTO;
import animal_shop.global.pay.dto.KakaoReadyResponse;
import animal_shop.shop.delivery.dto.DeliveryRevokeDTO;
import animal_shop.shop.delivery.dto.DeliveryRevokeResponse;
import animal_shop.shop.order.dto.MyOrderCountDTO;
import animal_shop.shop.order.dto.OrderCancelDTO;
import animal_shop.shop.order.dto.OrderDTOList;
import animal_shop.shop.order.service.OrderService;
import animal_shop.shop.order_item.dto.OrderHistDTOResponse;
import io.swagger.v3.oas.annotations.Operation;
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
                                   @RequestBody OrderDTOList orderDTOList){
        ResponseDTO responseDTO;

        try{
            KakaoReadyResponse kakaoReadyResponse = orderService.order(orderDTOList,token);

            return ResponseEntity.ok().body(kakaoReadyResponse);
        }catch (Exception e){
            responseDTO = ResponseDTO.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @GetMapping("/orders")
    public ResponseEntity<?> orderHist(@RequestHeader(value = "Authorization") String token,
                                       @RequestParam(value = "page" , defaultValue = "1")int page,
                                       @RequestParam(value="status", required = false) String status){
        ResponseDTO responseDTO;
        try{
            OrderHistDTOResponse  orderHistDTOResponse = orderService.getOrderList(token,page-1, status);
                return ResponseEntity.ok().body(orderHistDTOResponse);
        }catch(Exception e){
            responseDTO = ResponseDTO.builder()
                    .error(e.getMessage())
                    .build();

            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    //주문 전체 취소
    @PatchMapping("/order/cancel")
    public ResponseEntity<?> cancelOrder (@RequestHeader(value = "Authorization") String token,
                                          @RequestBody OrderCancelDTO orderCancelDTO){
        ResponseDTO responseDTO;
        try{
            DeliveryRevokeResponse deliveryRevokeResponse = orderService.cancelOrder(token, orderCancelDTO);

            return ResponseEntity.ok().body(deliveryRevokeResponse);
        }catch(Exception e){
            responseDTO = ResponseDTO.builder()
                    .error(e.getMessage())
                    .build();

            return ResponseEntity.badRequest().body(responseDTO);
        }
    }


    @PatchMapping("/order/cancel_detail")
    public ResponseEntity<?> cancelOrderDetail (@RequestHeader(value = "Authorization") String token,
                                                @RequestBody DeliveryRevokeDTO deliveryRevokeDTO){
        ResponseDTO responseDTO;
        try{
            DeliveryRevokeResponse deliveryRevokeResponse = orderService.cancelOrderDetail(token, deliveryRevokeDTO);

            return ResponseEntity.ok().body(deliveryRevokeResponse);
        }catch(Exception e){
            responseDTO = ResponseDTO.builder()
                    .error(e.getMessage())
                    .build();

            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @GetMapping("/order_info")
    @Operation(
            summary = "해당 멤버가 주문한 물품들의 상태 수",
            description = "특정 멤버가 지금까지 주문하거나 취소 혹은 수신받은 모든 개별 상품들의 수를 출력합니다.")

    public ResponseEntity<?> myOrderInfo(@RequestHeader(value = "Authorization") String token){
        ResponseDTO responseDTO;
        try{
            MyOrderCountDTO myOrderCountDTO = orderService.getOrderInfo(token);

            return ResponseEntity.ok().body(myOrderCountDTO);
        }catch(Exception e){
            responseDTO = ResponseDTO.builder()
                    .error(e.getMessage())
                    .build();

            return ResponseEntity.badRequest().body(responseDTO);
        }
    }
}
