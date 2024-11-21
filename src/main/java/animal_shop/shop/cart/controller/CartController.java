package animal_shop.shop.cart.controller;

import animal_shop.global.dto.ResponseDTO;
import animal_shop.shop.cart.dto.CartDetailDTOResponse;
import animal_shop.shop.cart.dto.CartItemDTO;
import animal_shop.shop.cart.service.CartService;
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
}
