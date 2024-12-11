package animal_shop.tools.chat.controller;

import animal_shop.tools.chat.dto.ChatDTO;
import animal_shop.tools.chat.dto.ChatRoomDTO;
import animal_shop.tools.chat.entity.ChatRoom;
import animal_shop.tools.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/chat")
public class ChatController {
    private final ChatService chatService;

    @PostMapping("/room")
    public ResponseEntity<ChatRoom> createChatRoom(@RequestBody ChatRoomDTO chatRoomDto) {
        ChatRoom chatRoom = chatService.ensureChatRoom(chatRoomDto.getBuyerId(), chatRoomDto.getBuyerNickname(),
                chatRoomDto.getSellerId(), chatRoomDto.getSellerNickname());
        return ResponseEntity.ok(chatRoom);
    }

    @MessageMapping("/send/{chatRoomId}")
    @SendTo("/topic/chat/{chatRoomId}")
    public ChatDTO sendMessage(@Payload ChatDTO chatDTO, @DestinationVariable String chatRoomId) {
        chatDTO.setChatRoomId(chatRoomId);
        ChatDTO savedChat = chatService.saveChat(chatDTO);
        log.debug("Message sent to room {}: {}", chatRoomId, savedChat);
        return savedChat;
    }

    @GetMapping("/room/{chatRoomId}")
    public ResponseEntity<List<ChatDTO>> getChatsByRoomId(@PathVariable String chatRoomId) {
        List<ChatDTO> chats = chatService.getChatsByRoomId(chatRoomId);
        return ResponseEntity.ok(chats);
    }
}