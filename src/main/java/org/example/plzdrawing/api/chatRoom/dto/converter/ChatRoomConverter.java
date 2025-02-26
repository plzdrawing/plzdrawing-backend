package org.example.plzdrawing.api.chatRoom.dto.converter;

import org.example.plzdrawing.api.chatRoom.dto.response.ResponseChatRoom;
import org.example.plzdrawing.domain.chat.room.ChatRoom;

public class ChatRoomConverter {
    public static ResponseChatRoom fromEntity(ChatRoom chatRoom, String counterpartNickname) {
        return new ResponseChatRoom(
                chatRoom.getChatRoomId(),
                counterpartNickname,
                chatRoom.getLastMessage()
        );
    }
}
