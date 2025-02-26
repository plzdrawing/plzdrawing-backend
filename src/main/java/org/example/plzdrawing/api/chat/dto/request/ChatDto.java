package org.example.plzdrawing.api.chat.dto.request;

import lombok.Getter;
import org.example.plzdrawing.domain.chat.message.MessageType;

@Getter
public class ChatDto {

    private String chatRoomId;
    private Long senderId;
    private String message;
    private MessageType messageType;

    private String fileUrl;
    private String fileName;
    private Long fileSize;
    private String mimeType;
}
