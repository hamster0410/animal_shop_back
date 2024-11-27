package animal_shop.shop.cart.controller;

import animal_shop.global.dto.ResponseDTO;
import animal_shop.shop.cart.dto.CartDetailDTOResponse;
import animal_shop.shop.cart.dto.CartItemDTO;
import animal_shop.shop.cart.dto.CartItemUpdateDTO;
import animal_shop.shop.cart.service.CartService;
import animal_shop.shop.cart_item.dto.CartItemDetailRequest;
import animal_shop.shop.cart_item.dto.CartItemDetailResponse;
import animal_shop.shop.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private OrderService orderService;

    @PostMapping(value = "/add")
    public ResponseEntity<?> order(
            @RequestHeader(value = "Authorization") String token,
            @RequestBody CartItemDTO cartItemDTO
            ){
        ResponseDTO responseDTO;
        Long cartItemId;
        try{
            cartItemId = cartService.addCart(cartItemDTO,token);

            return ResponseEntity.ok().body(cartItemId);
        }catch(Exception e){
            responseDTO = ResponseDTO.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @GetMapping("/list")
    public ResponseEntity<?> orderHist(@RequestHeader(value = "Authorization") String token){
        ResponseDTO responseDTO;
        try{
            CartDetailDTOResponse cartDetailDTOResponse = cartService.getCartList(token);

            return ResponseEntity.ok().body(cartDetailDTOResponse);
        }catch (Exception e){
            responseDTO = ResponseDTO.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }
    @PostMapping("/detail/{cartItemId}")
    public ResponseEntity<?> cartItemDetail(@RequestHeader(value = "Authorization")String token,
                                            @PathVariable(value = "cartItemId") Long cartItemId,
                                            @RequestBody CartItemDetailRequest cartItemDetailRequest){
        ResponseDTO responseDTO;
        try{
            CartItemDetailResponse cartItemDetailResponse = cartService.getCartItemDetail(cartItemId,cartItemDetailRequest);

            return ResponseEntity.ok().body(cartItemDetailResponse);
        }catch(Exception e){
            responseDTO = ResponseDTO.builder()
                    .error(e.getMessage())
                    .build();

            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @PatchMapping("/update/{cartItemId}")
    public ResponseEntity<?> cartItemUpdate(@RequestHeader(value = "Authorization")String token,
                                            @PathVariable(value = "cartItemId") Long cartItemId,
                                            @RequestBody CartItemUpdateDTO cartItemUpdateDTO){
        ResponseDTO responseDTO;
        try{
            cartService.updateCartItemDetail(cartItemId, cartItemUpdateDTO);
            responseDTO = ResponseDTO.builder()
                    .message("CartItem change success")
                    .build();
            return ResponseEntity.ok().body(responseDTO);
        }catch(Exception e){
            responseDTO = ResponseDTO.builder()
                    .error(e.getMessage())
                    .build();

            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @DeleteMapping("/delete/{cartItemId}")
    public ResponseEntity<?> cartItemDelete(@RequestHeader(value = "Authorization")String token,
                                            @PathVariable(value = "cartItemId") Long cartItemId){
        ResponseDTO responseDTO;
        try{
            cartService.deleteCartItemDetail(cartItemId);
            responseDTO = ResponseDTO.builder()
                    .message("CartItem delete success")
                    .build();
            return ResponseEntity.ok().body(responseDTO);
        }catch(Exception e){
            responseDTO = ResponseDTO.builder()
                    .error(e.getMessage())
                    .build();

            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @PostMapping("/orders")
    public ResponseEntity<?> orderCartItem(@RequestHeader(value = "Authorization")String token,
                                           @RequestBody CartDetailDTOResponse cartDetailDTOResponse){
        ResponseDTO responseDTO;
        try{
            orderService.orderCart(token,cartDetailDTOResponse);
            cartService.emptyCart(cartDetailDTOResponse);
            responseDTO = ResponseDTO.builder()
                    .message("order success")
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
