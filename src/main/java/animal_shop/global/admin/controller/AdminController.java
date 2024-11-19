package animal_shop.global.admin.controller;

import animal_shop.global.admin.dto.SellerResponseDTO;
import animal_shop.global.admin.service.AdminService;
import animal_shop.global.dto.ResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    @Autowired
    AdminService adminService;

    @GetMapping("/request-list")
    public ResponseEntity<?> requestPage(@RequestHeader(value = "Authorization") String token, @RequestParam(value = "page", defaultValue = "1") int page){
        ResponseDTO responseDTO = null;
        try{
            SellerResponseDTO sellerDTOS = adminService.request_list(page-1);

            return ResponseEntity.ok().body(sellerDTOS);
        }catch(Exception e){
            responseDTO = ResponseDTO.builder()
                    .error(e.getMessage()).build();
            return ResponseEntity
                    .badRequest()
                    .body(responseDTO);
        }
    }

    @PatchMapping("/seller-ok")
    public ResponseEntity<?> sellerOk(@RequestHeader(value = "Authorization") String token, @RequestParam(value = "username") String username){
        ResponseDTO responseDTO = null;
        try{
            adminService.permit_seller(token,username);
            responseDTO = ResponseDTO.builder()
                    .message("seller permit success")
                    .build();

            return ResponseEntity.ok().body(responseDTO);
        }catch(Exception e){
            responseDTO = ResponseDTO.builder()
                    .error(e.getMessage()).build();
            return ResponseEntity
                    .badRequest()
                    .body(responseDTO);
        }
    }

    @PatchMapping("/seller-revoke")
    public ResponseEntity<?> sellerRevoke(@RequestHeader("Authorization") String token,@RequestParam(value = "username") String username){
        ResponseDTO responseDTO;

        try{
            adminService.revoke_seller(token, username);
            responseDTO = ResponseDTO.builder().message("revoke success").build();
            return ResponseEntity.ok().body(responseDTO);

        }catch(Exception e){
            responseDTO = ResponseDTO.builder()
                    .error(e.getMessage()).build();
            return ResponseEntity
                    .badRequest()
                    .body(responseDTO);
        }
    }

    @DeleteMapping("/seller-delete")
    public ResponseEntity<?> sellerDelete(@RequestHeader("Authorization") String token,@RequestParam(value = "username") String username){
        ResponseDTO responseDTO;

        try{
            adminService.delete_seller(token, username);
            responseDTO = ResponseDTO.builder().message("delete success").build();
            return ResponseEntity.ok().body(responseDTO);

        }catch(Exception e){
            responseDTO = ResponseDTO.builder()
                    .error(e.getMessage()).build();
            return ResponseEntity
                    .badRequest()
                    .body(responseDTO);
        }
    }
}
