package animal_shop.tools.chat.entity;

import animal_shop.tools.chat.dto.ChatDto;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Document(collection = "chats")
@Getter
@Setter
@Data
@Builder
public class Chat {
    @Id
    private String id;

    private String senderId;
    private String senderNickname;
    private String recipientId;
    private String recipientNickname;
    private String roomId;
    private String message;

    @CreatedDate
    private LocalDateTime date;

    public ChatDto toDto() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss");
        return ChatDto.builder()
                .senderId(getSenderId())
                .senderNickname(getSenderNickname())
                .recipientId(getRecipientId())
                .recipientNickname(getRecipientNickname())
                .message(getMessage())
                .date(getDate().format(formatter))
                .build();
    }
}