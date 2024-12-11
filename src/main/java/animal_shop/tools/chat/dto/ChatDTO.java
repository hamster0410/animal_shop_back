package animal_shop.tools.chat.dto;

import animal_shop.tools.chat.entity.Chat;
import lombok.*;

import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatDTO {
    private String id;
    private String chatRoomId;
    private Long senderId;
    private Long recipientId;
    private String senderNickname;
    private String recipientNickname;
    private String message;
    private OffsetDateTime date;

    public static Chat convertToEntity(ChatDTO chatDTO) {
        return Chat.builder()
                .id(chatDTO.getId())
                .chatRoomId(chatDTO.getChatRoomId())
                .senderId(chatDTO.getSenderId())
                .senderNickname(chatDTO.getSenderNickname())
                .message(chatDTO.getMessage())
                .date(chatDTO.getDate().toLocalDateTime())
                .build();
    }


}