package animal_shop.community.member.controller;

import animal_shop.community.member.dto.MemberDTO;
import animal_shop.community.member.service.MemberService;
import animal_shop.global.dto.ResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/mypage")
public class MyPageController {

    @Autowired
    private MemberService memberService;

    @GetMapping("/update")
    public ResponseEntity<?> memberInfo(@RequestHeader("Authorization") String token){
        try{
            MemberDTO memberDto = memberService.getByToken(token);
            return ResponseEntity.ok().body(memberDto);
        }catch (Exception e){
            log.error("mypage failed  error: {}", e.getMessage());

            ResponseDTO responseDTO = ResponseDTO.builder()
                    .error("mypage failed.")
                    .build();

            return ResponseEntity
                    .badRequest()
                    .body(responseDTO);
        }
    }

    @PostMapping("/update")
    public ResponseEntity<?> memberModify(@RequestHeader("Authorization") String token, @RequestBody MemberDTO memberDTO){
        ResponseDTO responseDTO;
        try{
            memberService.modify(memberDTO, token);
            responseDTO = ResponseDTO.builder().message("modify success").build();
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
