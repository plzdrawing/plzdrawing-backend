package org.example.plzdrawing.api.chat.repository;

import static org.example.plzdrawing.common.redis.RedisKeyPrefix.CHATROOM_PRESENCE;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatPresenceRepository {

    private final RedisTemplate<String, String> redisTemplate;

    public void setActive(String chatRoomId, Long memberId) {
        redisTemplate.opsForValue().set(getKey(memberId), chatRoomId);
    }

    public void removeActive(Long memberId) {
        redisTemplate.delete(getKey(memberId));
    }

    public boolean isActive(String chatRoomId, Long memberId) {
        String key = getKey(memberId);
        return redisTemplate.hasKey(key)
                && redisTemplate.opsForValue().get(key).equals(chatRoomId);
    }

    private String getKey(Long memberId) {
        return CHATROOM_PRESENCE + memberId;
    }
}
