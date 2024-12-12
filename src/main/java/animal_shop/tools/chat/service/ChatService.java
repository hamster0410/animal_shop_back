package animal_shop.tools.chat.service;

import animal_shop.community.member.repository.MemberRepository;
import animal_shop.global.service.GlobalService;
import animal_shop.tools.chat.dto.ChatDTO;
import animal_shop.tools.chat.entity.Chat;
import animal_shop.tools.chat.entity.ChatRoom;
import animal_shop.tools.chat.repository.ChatRepository;
import animal_shop.tools.chat.repository.ChatRoomRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRepository chatRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    GlobalService globalService;

    @Transactional
    public ChatRoom ensureChatRoom(String userId) {
        Long sellerId = 1L;
        Long buyerId = Long.valueOf(userId);
        String buyerNickname = globalService.getNickname(buyerId);
        String sellerNickname = globalService.getNickname(sellerId);
        return chatRoomRepository.findByBuyerIdAndSellerId(buyerId, sellerId)
                .orElseGet(() -> {
                    ChatRoom newRoom = ChatRoom.builder()
                            .id(UUID.randomUUID().toString())
                            .buyerId(buyerId)
                            .buyerNickname(buyerNickname)
                            .sellerId(sellerId)
                            .sellerNickname(sellerNickname)
                            .build();
                    return chatRoomRepository.save(newRoom);
                });
    }

    public List<ChatDTO> getChatsByRoomId(String chatRoomId) {
        List<Chat> chats = chatRepository.findByChatRoomId(chatRoomId);
        List<ChatDTO> chatDtos = new ArrayList<>();
        for (Chat chat : chats) {
            chatDtos.add(Chat.convertToDTO(chat));
        }
        return chatDtos;
    }

    public ChatDTO saveChat(ChatDTO chatDto) {
        Chat chat = ChatDTO.convertToEntity(chatDto);
        chat = chatRepository.save(chat);
        return Chat.convertToDTO(chat);
    }
}