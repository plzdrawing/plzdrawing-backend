package org.example.plzdrawing.domain.chat;

import java.sql.Timestamp;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Document(collection = "chat_messages")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Chat {

    @Id
    private String chatId;

    private String chatRoomId;

    private Long senderId;
    private String message;
    private MessageType messageType;
    private boolean isRead;

    private String fileUrl;
    private String fileName;
    private Long fileSize;
    private String mimeType;

    private Timestamp timestamp;

    @Builder
    private Chat(String chatRoomId, Long senderId, String message, MessageType messageType,
            String fileUrl, String fileName, Long fileSize, String mimeType,
            Timestamp timestamp) {
        this.chatRoomId = chatRoomId;
        this.senderId = senderId;
        this.message = message;
        this.messageType = messageType;
        this.isRead = true;
        this.fileUrl = fileUrl;
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.mimeType = mimeType;
        this.timestamp = timestamp;
    }

    public String getDisplayContent() {
        return messageType.getDisplayContent(message);
    }

    public void read(boolean b) {
        isRead = b;
    }
}
