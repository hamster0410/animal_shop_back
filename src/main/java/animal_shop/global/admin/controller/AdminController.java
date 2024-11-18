package animal_shop.global.admin.controller;

import animal_shop.global.dto.ResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class AdminController {

//    @GetMapping("/request-list")
//    public ResponseEntity<?> requestPage(@RequestHeader(value = "Authorization") String token){
//        ResponseDTO responseDTO;
//        try{
//
//        }catch(Exception e){
//
//        }
//    }
}
