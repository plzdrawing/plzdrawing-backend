package org.example.plzdrawing.util.security.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.example.plzdrawing.api.auth.customuser.CustomUser;
import org.example.plzdrawing.domain.chat.room.ChatRoom;
import org.example.plzdrawing.domain.chat.room.ChatRoomRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class ChatRoomMembershipInterceptor implements HandlerInterceptor {

    private final ChatRoomRepository chatRoomRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        String chatRoomId = extractChatRoomId(request, response);
        if (chatRoomId == null) {
            return false;
        }

        CustomUser customUser = extractCustomUser(response);
        if (customUser == null) {
            return false;
        }
        Long memberId = customUser.getMemberId();

        Optional<ChatRoom> chatRoom = chatRoomRepository.findById(chatRoomId);
        if (chatRoom.isEmpty()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "채팅방이 존재하지 않습니다.");
            return false;
        }

        if (!isUserMemberOfChatRoom(memberId, chatRoom.get())) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "접근 권한이 없습니다.");
            return false;
        }

        // TODO: 캐시, 세션 등 최적화
        return true;
    }

    private String extractChatRoomId(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String uri = request.getRequestURI();
        String[] parts = uri.split("/");
        if (parts.length < 4) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "잘못된 요청입니다.");
            return null;
        }
        return parts[3];
    }

    private CustomUser extractCustomUser(HttpServletResponse response) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof CustomUser customUser)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "권한 정보가 잘못되었습니다.");
            return null;
        }
        return customUser;
    }

    private boolean isUserMemberOfChatRoom(Long memberId, ChatRoom chatRoom) {
        return memberId.equals(chatRoom.getSellerId()) || memberId.equals(chatRoom.getBuyerId());
    }
}
