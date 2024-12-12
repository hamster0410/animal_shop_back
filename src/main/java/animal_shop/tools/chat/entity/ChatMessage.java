package animal_shop.tools.chat.entity;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder
public class ChatMessage {

    private String roomId;
    private String sender;
    private String message;
    private String sendingTime;
}
