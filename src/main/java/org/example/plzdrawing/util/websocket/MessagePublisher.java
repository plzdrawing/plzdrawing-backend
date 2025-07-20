package org.example.plzdrawing.util.websocket;

import lombok.RequiredArgsConstructor;
import org.example.plzdrawing.common.listener.websocket.dto.JoinEvent;
import org.example.plzdrawing.domain.chat.Chat;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessagePublisher {

    private final SimpMessagingTemplate messagingTemplate;

    public void publishNotExist(String chatRoomId, Chat chat) {
        messagingTemplate.convertAndSend("/topic/unread/"+chatRoomId, chat.getDisplayContent());
    }
    public void publishExist(String chatRoomId, Chat chat) {
        messagingTemplate.convertAndSend("/topic/join/" + chatRoomId, chat);
    }
    public void removeUnreadCheck(String chatRoomId, Long memberId) {
        JoinEvent joinEvent = new JoinEvent(chatRoomId, memberId);
        messagingTemplate.convertAndSend("/topic/join/" + chatRoomId, joinEvent);
    }
}
