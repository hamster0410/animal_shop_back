package animal_shop.shop.cart.controller;

import animal_shop.global.dto.ResponseDTO;
import animal_shop.shop.cart.dto.CartDetailDTOResponse;
import animal_shop.shop.cart.dto.CartItemDTO;
import animal_shop.shop.cart.dto.CartItemUpdateDTO;
import animal_shop.shop.cart.service.CartService;
import animal_shop.shop.cart_item.dto.CartItemDetailRequest;
import animal_shop.shop.cart_item.dto.CartItemDetailResponse;
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
    public ResponseEntity<?> orderHist(@RequestHeader(value = "Authorization") String token,
                            @RequestParam(value = "page",  defaultValue = "1") int page){
        ResponseDTO responseDTO;
        try{
            CartDetailDTOResponse cartDetailDTOResponse = cartService.getCartList(token,page-1);

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

    @PatchMapping("cart/update/{cartItemId}")
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
}
