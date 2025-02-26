package org.example.plzdrawing.api.chatRoom.dto.response;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ResponseChatRoom {

    private String chatRoomId;
    private String counterpartNickname;
    private String lastMessage;
}
