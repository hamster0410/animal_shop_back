package animal_shop.tools.chat.controller;

import animal_shop.tools.chat.dto.ChatDto;
import animal_shop.tools.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatController {
    private final ChatService chatService;

    @PostMapping("/send")
    public ChatDto sendChat(@RequestBody ChatDto chatDto) {
        return chatService.saveChat(chatDto);
    }

    @GetMapping("/room/{roomId}")
    public List<ChatDto> getChatsByRoomId(@PathVariable String roomId) {
        return chatService.getChatsByRoomId(roomId);
    }
}
