package animal_shop.tools.chat.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatRoomDTO {
    private String id;
    private Long buyerId;
    private String buyerNickname;
    private Long sellerId;
    private String sellerNickname;
    private String lastMessage;
    private LocalDateTime lastMessageTime;
    private String sellerProfile;
    private String buyerProfile;

    public String getChatPartnerNickname(Long userId) {
        return userId.equals(buyerId) ? sellerNickname : buyerNickname;
    }
}