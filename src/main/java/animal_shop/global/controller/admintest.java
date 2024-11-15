package animal_shop.global.controller;

import animal_shop.global.dto.ResponseDTO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class admintest {

    @GetMapping("/")
    public ResponseDTO testadmin(){
        return ResponseDTO.builder()
                .message("success")
                        .build();
    }
}
