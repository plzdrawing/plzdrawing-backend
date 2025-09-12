package org.example.plzdrawing.api.chat.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import org.example.plzdrawing.api.chat.dto.validation.ValidChatDto;
import org.example.plzdrawing.domain.chat.MessageType;

@Getter
@ValidChatDto
public class ChatDto {

    @Positive
    private String chatRoomId;

    @Positive
    private Long senderId;

    @NotBlank
    private String message;

    @NotBlank
    private MessageType messageType;

    private String fileUrl;
    private String fileName;
    private Long fileSize;
    private String mimeType;
}
