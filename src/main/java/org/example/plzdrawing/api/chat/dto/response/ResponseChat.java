package org.example.plzdrawing.api.chat.dto.response;

import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import org.example.plzdrawing.domain.chat.MessageType;

@AllArgsConstructor
public class ResponseChat {

    private String chatId;
    private String chatRoomId;
    private Long senderId;
    private String message;
    private Timestamp timestamp;
    private MessageType messageType;
    private String FileUrl;
    private String FileName;
    private Long FileSize;
    private String mimeType;
}
