package org.example.plzdrawing.common.listener.websocket;

import lombok.RequiredArgsConstructor;
import org.example.plzdrawing.api.chat.repository.ChatPresenceRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@RequiredArgsConstructor
public class ChatDisconnectEventListener implements ApplicationListener<SessionDisconnectEvent> {

    private final ChatPresenceRepository chatPresenceRepository;

    @Override
    public void onApplicationEvent(SessionDisconnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        Long memberId = Long.parseLong(accessor.getFirstNativeHeader("memberId"));
        chatPresenceRepository.removeActive(memberId);
    }
}
