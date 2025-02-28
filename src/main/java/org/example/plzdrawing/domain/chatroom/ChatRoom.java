package org.example.plzdrawing.domain.chatroom;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.plzdrawing.domain.member.Member;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Document(collection = "chat_rooms")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoom {

    @Id
    private String chatRoomId;

    private Long sellerId;
    private Long buyerId;
    private String sellerNickname;
    private String buyerNickname;

    private String lastMessage;

    @Builder
    private ChatRoom(Long sellerId, Long buyerId, String sellerNickname,
            String buyerNickname, String lastMessage) {
        this.sellerId = sellerId;
        this.buyerId = buyerId;
        this.sellerNickname = sellerNickname;
        this.buyerNickname = buyerNickname;
        this.lastMessage = lastMessage;
    }

    public void updateLastMessage(String message) {
        this.lastMessage = message;
    }

    public static ChatRoom create(Member seller, Member buyer) {
        return ChatRoom.builder()
                .buyerId(buyer.getId())
                .buyerNickname(buyer.getNickname())
                .sellerId(seller.getId())
                .sellerNickname(seller.getNickname())
                .lastMessage("")
                .build();
    }
}
