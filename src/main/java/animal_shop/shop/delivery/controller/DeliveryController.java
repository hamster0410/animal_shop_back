package animal_shop.shop.delivery.controller;

import animal_shop.global.dto.ResponseDTO;
import animal_shop.shop.delivery.dto.DeliveryCheckDTO;
import animal_shop.shop.delivery.dto.DeliveryCustomerResponse;
import animal_shop.shop.delivery.service.DeliveryService;
import animal_shop.shop.order_item.dto.OrderHistDTOResponse;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/delivery")
public class DeliveryController {

    @Autowired
    DeliveryService deliveryService;

    @GetMapping("/progress-list")
    ResponseEntity<?> getDeliveryList(@RequestHeader(value = "Authorization") String token,
                                      @RequestParam(name = "page", defaultValue = "1")int page){
        ResponseDTO responseDTO;
        try{
            OrderHistDTOResponse deliveryCustomerResponse = deliveryService.get_deliveryList(token,page-1);
            return ResponseEntity.ok().body(deliveryCustomerResponse);
        }catch (Exception e){
            responseDTO = ResponseDTO
                    .builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @GetMapping("/completed-list")
    ResponseEntity<?> getDeliveryCompletedList(@RequestHeader(value = "Authorization") String token,
                                      @RequestParam(name = "page", defaultValue = "1")int page){
        ResponseDTO responseDTO;
        try{
            OrderHistDTOResponse deliveryCustomerResponse = deliveryService.get_deliveryCompltedList(token,page-1);
            return ResponseEntity.ok().body(deliveryCustomerResponse);
        }catch (Exception e){
            responseDTO = ResponseDTO
                    .builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @PostMapping("/delivery-check")
    ResponseEntity<?> getDeliveryList(
            @RequestHeader(value = "Authorization") String token,
            @RequestBody DeliveryCheckDTO deliveryCheckDTO){
        ResponseDTO responseDTO;
        try{
            deliveryService.delivery_check(token, deliveryCheckDTO);
            responseDTO = ResponseDTO
                    .builder()
                    .message("delivery check success")
                    .build();
            return ResponseEntity.ok().body(responseDTO);
        }catch (Exception e){
            responseDTO = ResponseDTO
                    .builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }
}
