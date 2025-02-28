package org.example.plzdrawing.api.chat.dto.converter;

import java.sql.Timestamp;
import org.example.plzdrawing.api.chat.dto.request.ChatDto;
import org.example.plzdrawing.api.chat.dto.response.ResponseChat;
import org.example.plzdrawing.domain.chat.Chat;

public class ChatConverter {

    public static Chat toEntity(ChatDto dto, Timestamp sendTime) {
        return Chat.builder()
                .chatRoomId(dto.getChatRoomId())
                .senderId(dto.getSenderId())
                .message(dto.getMessage())
                .messageType(dto.getMessageType())
                .fileUrl(dto.getFileUrl())
                .fileName(dto.getFileName())
                .fileSize(dto.getFileSize())
                .mimeType(dto.getMimeType())
                .timestamp(sendTime)
                .build();
    }

    public static ResponseChat fromEntity(Chat chat) {
        return new ResponseChat(
                chat.getChatId(),
                chat.getChatRoomId(),
                chat.getSenderId(),
                chat.getMessage(),
                chat.getTimestamp(),
                chat.getMessageType(),
                chat.getFileUrl(),
                chat.getFileName(),
                chat.getFileSize(),
                chat.getMimeType()
        );
    }
}