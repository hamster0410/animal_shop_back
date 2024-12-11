package animal_shop.tools.chat.entity;

import animal_shop.tools.chat.dto.ChatDTO;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Document(collection = "chats")
@Getter
@Setter
@Data
@Builder
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    private Long senderId;
    private String senderNickname;
    private Long recipientId;
    private String recipientNickname;
    private String chatRoomId;
    private String message;

    @CreatedDate
    private LocalDateTime date;

    public ChatDTO toDto() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss");
        return ChatDTO.builder()
                .senderId(getSenderId())
                .senderNickname(getSenderNickname())
                .recipientId(getRecipientId())
                .recipientNickname(getRecipientNickname())
                .message(getMessage())
                .date(OffsetDateTime.parse(getDate().format(formatter)))
                .build();
    }

    public static ChatDTO convertToDTO (Chat chat) {
        return ChatDTO.builder()
                .id(chat.getId())
                .chatRoomId(chat.getChatRoomId())
                .senderId(chat.getSenderId())
                .senderNickname(chat.getSenderNickname())
                .message(chat.getMessage())
                .date(chat.getDate().atZone(ZoneId.of("Asia/Seoul")).toOffsetDateTime())
                .build();
    }
}