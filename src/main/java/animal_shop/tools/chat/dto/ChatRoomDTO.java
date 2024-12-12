package animal_shop.tools.chat.dto;

import lombok.*;

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

    public String getChatPartnerNickname(Long userId) {
        return userId.equals(buyerId) ? sellerNickname : buyerNickname;
    }
}