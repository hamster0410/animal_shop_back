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
            System.out.println("check 1");
            SellerResponseDTO sellerDTOS = adminService.request_list(page-1);

            return ResponseEntity.ok().body(sellerDTOS);
        }catch(Exception e){
            responseDTO = ResponseDTO.builder()
                    .error("main_list fail").build();
            return ResponseEntity
                    .badRequest()
                    .body(responseDTO);
        }
    }
}
