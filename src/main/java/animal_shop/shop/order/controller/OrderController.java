package animal_shop.shop.order.controller;

import animal_shop.global.dto.ResponseDTO;
import animal_shop.shop.order.dto.OrderDTOList;
import animal_shop.shop.order.dto.OrderDTOResponse;
import animal_shop.shop.order.service.OrderService;
import animal_shop.shop.order_item.dto.OrderHistDTOResponse;
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
            OrderDTOResponse order = orderService.order(orderDTOList,token);
            return ResponseEntity.ok().body(order);
        }catch (Exception e){
            responseDTO = ResponseDTO.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @GetMapping("/orders")
    public ResponseEntity<?> orderHist(@RequestHeader(value = "Authorization") String token,
                                       @RequestParam(value = "page" , defaultValue = "1")int page){
        ResponseDTO responseDTO;
        try{
            OrderHistDTOResponse  orderHistDTOResponse = orderService.getOrderList(token,page-1);
                return ResponseEntity.ok().body(orderHistDTOResponse);
        }catch(Exception e){
            responseDTO = ResponseDTO.builder()
                    .error(e.getMessage())
                    .build();

            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @PatchMapping("/order/cancel/{orderId}")
    public ResponseEntity<?> cancelOrder (@RequestHeader(value = "Authorization") String token,
                                          @PathVariable(value = "orderId") Long orderId){
        ResponseDTO responseDTO;
        try{
            orderService.cancelOrder(token, orderId);
            responseDTO = ResponseDTO.builder()
                    .message("cancel success")
                    .build();

            return ResponseEntity.ok().body(responseDTO);
        }catch(Exception e){
            responseDTO = ResponseDTO.builder()
                    .error(e.getMessage())
                    .build();

            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

}
