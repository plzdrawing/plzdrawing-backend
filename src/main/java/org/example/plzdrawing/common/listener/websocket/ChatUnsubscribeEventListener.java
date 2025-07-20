package org.example.plzdrawing.common.listener.websocket;

import lombok.RequiredArgsConstructor;
import org.example.plzdrawing.api.chat.repository.ChatPresenceRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

@Component
@RequiredArgsConstructor
public class ChatUnsubscribeEventListener implements ApplicationListener<SessionUnsubscribeEvent> {

    private final ChatPresenceRepository chatPresenceRepository;

    @Override
    public void onApplicationEvent(SessionUnsubscribeEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String destination = accessor.getDestination();
        if (destination.startsWith("/topic/join/")) {
            Long memberId = Long.parseLong(accessor.getFirstNativeHeader("memberId"));
            chatPresenceRepository.removeActive(memberId);
        }
    }
}