package animal_shop.tools.chat.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatDto {
    private String senderId;
    private String senderNickname;
    private String recipientId;
    private String recipientNickname;
    private String message;
    private String date;
}