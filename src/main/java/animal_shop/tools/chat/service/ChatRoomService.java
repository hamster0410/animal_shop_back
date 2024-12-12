package animal_shop.tools.chat.service;

import animal_shop.tools.chat.dto.ChatRoomDTO;
import animal_shop.tools.chat.entity.ChatRoom;
import animal_shop.tools.chat.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;

    public List<ChatRoomDTO> getChatRoomsByUserId(Long userId) {
        List<ChatRoom> chatRooms = chatRoomRepository.findByBuyerIdOrSellerId(userId, userId);
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

        return ChatRoomDTO.builder()
                .id(chatRoom.getId())
                .buyerId(chatRoom.getBuyerId())
                .buyerNickname(chatRoom.getBuyerNickname())
                .sellerId(chatRoom.getSellerId())
                .sellerNickname(chatRoom.getSellerNickname())
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
