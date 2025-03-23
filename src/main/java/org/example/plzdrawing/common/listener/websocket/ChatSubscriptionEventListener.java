package org.example.plzdrawing.common.listener.websocket;

import lombok.RequiredArgsConstructor;
import org.example.plzdrawing.api.chat.repository.ChatPresenceRepository;
import org.example.plzdrawing.api.chatRoom.repository.UnreadCountRepository;
import org.example.plzdrawing.util.websocket.MessagePublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

@Component
@RequiredArgsConstructor
public class ChatSubscriptionEventListener implements ApplicationListener<SessionSubscribeEvent> {

    private final ChatPresenceRepository chatPresenceRepository;
    private final UnreadCountRepository unreadCountRepository;
    private final MessagePublisher messagePublisher;

    @Override
    public void onApplicationEvent(SessionSubscribeEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String destination = accessor.getDestination();
        if (destination.startsWith("/topic/join/")) {
            String chatRoomId = destination.substring("/topic/join/".length());

            Long memberId = Long.parseLong(accessor.getFirstNativeHeader("memberId"));
            chatPresenceRepository.setActive(chatRoomId, memberId);
            unreadCountRepository.resetUnreadCount(chatRoomId, memberId);

            messagePublisher.removeUnreadCheck(chatRoomId, memberId);
        }
    }
}
