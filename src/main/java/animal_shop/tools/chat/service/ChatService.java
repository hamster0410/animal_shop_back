package animal_shop.tools.chat.service;

import animal_shop.tools.chat.dto.ChatDto;
import animal_shop.tools.chat.entity.Chat;
import animal_shop.tools.chat.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatRepository chatRepository;

    public ChatDto saveChat(ChatDto chatDto) {
        String roomId = UUID.randomUUID().toString();

        Chat chat = Chat.builder()
                .senderId(chatDto.getSenderId())
                .senderNickname(chatDto.getSenderNickname())
                .recipientId(chatDto.getRecipientId())
                .recipientNickname(chatDto.getRecipientNickname())
                .roomId(roomId)
                .message(chatDto.getMessage())
                .build();

        chat = chatRepository.save(chat);

        return chat.toDto();
    }

    public List<ChatDto> getChatsByRoomId(String roomId) {
        List<Chat> chats = chatRepository.findByRoomId(roomId);
        List<ChatDto> chatDtos = new ArrayList<>();
        for (Chat chat : chats) {
            chatDtos.add(chat.toDto());
        }

        return chatDtos;
    }
}