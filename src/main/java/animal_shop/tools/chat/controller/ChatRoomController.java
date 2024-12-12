package animal_shop.tools.chat.controller;

import animal_shop.global.security.TokenProvider;
import animal_shop.tools.chat.dto.ChatRoomDTO;
import animal_shop.tools.chat.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/chat-room")
public class ChatRoomController {
    private final ChatRoomService chatRoomService;

    @Autowired
    private TokenProvider tokenProvider;

    //현재 사용자가 속해있는 톡방
    @GetMapping("/mine")
    public ResponseEntity<List<ChatRoomDTO>> getChatRoomsByUserId(@RequestHeader("Authorization") String token) {
        String userId = tokenProvider.extractIdByAccessToken(token);
        if(userId == null) throw new IllegalArgumentException("userId error");
        List<ChatRoomDTO> chatRooms = chatRoomService.getChatRoomsByUserId(Long.valueOf(userId));
        return ResponseEntity.ok(chatRooms);
    }

    //특정 사용자가 속해있는 톡방
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ChatRoomDTO>> getChatRoomsBySpecificUserId(@PathVariable(name = "userId") Long userId) {
        List<ChatRoomDTO> chatRooms = chatRoomService.getChatRoomsByUserId(userId);
        return ResponseEntity.ok(chatRooms);
    }

    //특정 톡방 정보 추출
    @GetMapping("/{chatRoomId}")
    public ResponseEntity<ChatRoomDTO> getChatRoomById(@PathVariable String chatRoomId) {
        ChatRoomDTO chatRoom = chatRoomService.getChatRoomById(chatRoomId);
        return ResponseEntity.ok(chatRoom);
    }

    //모든 톡방 정보 추출
    @GetMapping("/list-all")
    public ResponseEntity<List<ChatRoomDTO>> Allchatting() {
        List<ChatRoomDTO> chatRooms = chatRoomService.getAllRoom();
        return ResponseEntity.ok(chatRooms);
    }
}