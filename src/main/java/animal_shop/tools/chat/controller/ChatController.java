package animal_shop.tools.chat.controller;

import animal_shop.global.security.TokenProvider;
import animal_shop.global.service.GlobalService;
import animal_shop.tools.chat.dto.ChatDTO;
import animal_shop.tools.chat.entity.ChatRoom;
import animal_shop.tools.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/chat")
public class ChatController {
    private final ChatService chatService;

    @Autowired
    TokenProvider tokenProvider;


    @Autowired
    GlobalService globalService;

    //채팅방 생성
    @GetMapping("/room")
    public ResponseEntity<ChatRoom> createChatRoom(@RequestHeader("Authorization") String token) {
        System.out.println("create chat room");
        if (token != null) {
            String userId = tokenProvider.extractIdByAccessToken(token);
            System.out.println("create room: " + userId);
            ChatRoom chatRoom = chatService.ensureChatRoom(userId);
            return ResponseEntity.ok(chatRoom);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    //대화 송신
    @MessageMapping("/send/{chatRoomId}")
    @SendTo("/topic/chat/{chatRoomId}")
    public ChatDTO sendMessage(@Payload ChatDTO chatDTO,
                               @DestinationVariable String chatRoomId,
                               StompHeaderAccessor stompHeaderAccessor) {
        // WebSocket 세션에서 userId를 가져오기
        String userId = (String) stompHeaderAccessor.getSessionAttributes().get("userId");
        if (userId == null) {
            userId = "unknown"; // 기본값 설정
        }
        // userId를 ChatDTO에 설정
        System.out.println("here is dto" + chatDTO.getChatRoomId());
        System.out.println("here is destination variable" + chatRoomId);
        chatDTO.setSenderId(Long.valueOf(userId));
        chatDTO.setSenderNickname(globalService.getNickname(Long.valueOf(userId)));
        chatDTO.setChatRoomId(chatRoomId);

        // 메시지 저장
        ChatDTO savedChat = chatService.saveChat(chatDTO);
        log.debug("Message sent to room {} by user {}: {}", chatRoomId, userId, savedChat);

        return savedChat;
    }

    //현재 대화 내용 출력
    @GetMapping("/room/{chatRoomId}")
    public ResponseEntity<List<ChatDTO>> getChatsByRoomId(@PathVariable String chatRoomId) {
        List<ChatDTO> chats = chatService.getChatsByRoomId(chatRoomId);
        return ResponseEntity.ok(chats);
    }
}