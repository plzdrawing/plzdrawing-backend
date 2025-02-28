package org.example.plzdrawing.api.chat.controller;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.example.plzdrawing.api.chat.dto.request.ChatDto;
import org.example.plzdrawing.api.chat.dto.response.ResponseChat;
import org.example.plzdrawing.api.chat.service.ChatService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chat")
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatService chatService;

    @MessageMapping("/v1/chat")    //app/v1/chat
    public void sendMessage(@Payload ChatDto chatDto) {
        chatService.saveMessage(chatDto, Timestamp.valueOf(LocalDateTime.now()));
        messagingTemplate.convertAndSend("/topic/"+chatDto.getChatRoomId(), chatDto);
    }

    @GetMapping("/{chatRoomId}")
    public ResponseEntity<Page<ResponseChat>> getChats(@PathVariable("chatRoomId") String chatRoomId,
            @RequestParam(name = "pageNum", defaultValue = "0") int pageNum) {
        Page<ResponseChat> chatPage = chatService.getChats(chatRoomId, pageNum);

        return ResponseEntity.ok(chatPage);
    }

    //TODO 정렬 인덱싱-날짜 / 채팅전송 validation
}
