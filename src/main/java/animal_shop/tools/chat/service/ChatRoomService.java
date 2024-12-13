package animal_shop.tools.chat.service;

import animal_shop.global.security.TokenProvider;
import animal_shop.global.service.GlobalService;
import animal_shop.tools.chat.dto.ChatRoomDTO;
import animal_shop.tools.chat.entity.Chat;
import animal_shop.tools.chat.entity.ChatRoom;
import animal_shop.tools.chat.repository.ChatRepository;
import animal_shop.tools.chat.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;

    private final ChatRepository chatRepository;

    private final GlobalService globalService;

    private final TokenProvider tokenProvider;

    public List<ChatRoomDTO> getChatRoomsBySelfId(String token) {
        String userId = tokenProvider.extractIdByAccessToken(token);
        if(userId == null) throw new IllegalArgumentException("userId error");

        List<ChatRoom> chatRooms = chatRoomRepository.findByBuyerId(Long.valueOf(userId));
        List<ChatRoomDTO> chatRoomDtos = new ArrayList<>();
        for (ChatRoom chatRoom : chatRooms) {
            chatRoomDtos.add(convertToDto(chatRoom));
        }
        return chatRoomDtos;
    }

    public List<ChatRoomDTO> getChatRoomsByUserId(String token,Long userId) {
        String adminId = tokenProvider.extractIdByAccessToken(token);
        if(!Objects.equals(adminId, "1")) throw new IllegalArgumentException("user is not admin");

        List<ChatRoom> chatRooms = chatRoomRepository.findByBuyerId(userId);
        List<ChatRoomDTO> chatRoomDtos = new ArrayList<>();
        for (ChatRoom chatRoom : chatRooms) {
            chatRoomDtos.add(convertToDto(chatRoom));
        }
        return chatRoomDtos;
    }

    public ChatRoomDTO getChatRoomById(String chatRoomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new RuntimeException("ChatRoom not found"));
        return convertToDto(chatRoom);
    }

    private ChatRoomDTO convertToDto(ChatRoom chatRoom) {
        Chat lastChat = chatRepository.findTopByChatRoomIdOrderByDateDesc(chatRoom.getId())
                .orElse(null);
        String message;
        LocalDateTime date;
        if(lastChat == null){
            message = "no message";
            date = null;
        }else{
            message = lastChat.getMessage();
            date = lastChat.getDate();
        }

        return ChatRoomDTO.builder()
                .id(chatRoom.getId())
                .buyerId(chatRoom.getBuyerId())
                .buyerNickname(chatRoom.getBuyerNickname())
                .sellerId(chatRoom.getSellerId())
                .sellerNickname(chatRoom.getSellerNickname())
                .lastMessage(message)
                .lastMessageTime(date)
                .buyerProfile(globalService.getProfile(chatRoom.getBuyerId()))
                .sellerProfile(globalService.getProfile(chatRoom.getSellerId()))
                .build();
    }

    public List<ChatRoomDTO> getAllRoom() {
        List<ChatRoom> chatRooms = chatRoomRepository.findAll();
        List<ChatRoomDTO> chatRoomDtos = new ArrayList<>();
        for (ChatRoom chatRoom : chatRooms) {
            chatRoomDtos.add(convertToDto(chatRoom));
        }
        return chatRoomDtos;
    }
}
