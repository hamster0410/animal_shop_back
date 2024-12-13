package animal_shop.global.controller;

import animal_shop.global.service.GlobalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    GlobalService globalService;

    @GetMapping("/testController")
    public ResponseEntity<?> testFunction(){
        globalService.getTestCode();
        return ResponseEntity.ok().body(null);
    }
}
