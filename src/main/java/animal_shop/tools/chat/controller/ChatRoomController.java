package animal_shop.tools.chat.controller;

import animal_shop.global.dto.ResponseDTO;
import animal_shop.tools.chat.dto.ChatRoomDTO;
import animal_shop.tools.chat.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/chat-room")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    //현재 사용자가 속해있는 톡방
    @GetMapping("/mine")
    public ResponseEntity<?> getChatRoomsByUserId(@RequestHeader("Authorization") String token) {
        ResponseDTO responseDTO;
        try{
            List<ChatRoomDTO> chatRooms = chatRoomService.getChatRoomsBySelfId(token);
            return ResponseEntity.ok(chatRooms);
        }catch (Exception e){
            responseDTO =ResponseDTO.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    //특정 사용자가 속해있는 톡방
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getChatRoomsBySpecificUserId(@RequestHeader("Authorization") String token,
                                                          @PathVariable(name = "userId") Long userId) {
        ResponseDTO responseDTO;
        try{
            List<ChatRoomDTO> chatRooms = chatRoomService.getChatRoomsByUserId(token,userId);
            return ResponseEntity.ok(chatRooms);
        }catch (Exception e){
            responseDTO =ResponseDTO.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    //특정 톡방 정보 추출
    @GetMapping("/{chatRoomId}")
    public ResponseEntity<?> getChatRoomById(@PathVariable String chatRoomId) {
        ResponseDTO responseDTO;
        try{
            ChatRoomDTO chatRoom = chatRoomService.getChatRoomById(chatRoomId);
            return ResponseEntity.ok(chatRoom);
        }catch (Exception e){
            responseDTO =ResponseDTO.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    //모든 톡방 정보 추출
    @GetMapping("/list-all")
    public ResponseEntity<?> Allchatting() {
        ResponseDTO responseDTO;
        try{
            List<ChatRoomDTO> chatRooms = chatRoomService.getAllRoom();
            return ResponseEntity.ok(chatRooms);
        }catch (Exception e){
            responseDTO =ResponseDTO.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }
}