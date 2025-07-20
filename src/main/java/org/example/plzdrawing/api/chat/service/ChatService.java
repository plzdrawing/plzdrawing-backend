package org.example.plzdrawing.api.chat.service;

import java.sql.Timestamp;
import org.example.plzdrawing.api.chat.dto.request.ChatDto;
import org.example.plzdrawing.api.chat.dto.response.ResponseChat;
import org.springframework.data.domain.Page;

public interface ChatService {

    void saveMessage(ChatDto chatDto, Timestamp sendTime);

    Page<ResponseChat> getChats(String chatRoomId, int pageNum);
}
