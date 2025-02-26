package org.example.plzdrawing.api.chatRoom.service;

import java.util.List;
import org.example.plzdrawing.api.chatRoom.dto.request.CreateChatRoomRequest;
import org.example.plzdrawing.api.chatRoom.dto.response.ResponseChatRoom;
import org.example.plzdrawing.domain.chat.message.Chat;

public interface ChatRoomService {

    List<ResponseChatRoom> getChatRooms(Long memberId);

    String createChatRoom(Long memberId, CreateChatRoomRequest request);

    void updateLastMessage(Chat chat);
}
