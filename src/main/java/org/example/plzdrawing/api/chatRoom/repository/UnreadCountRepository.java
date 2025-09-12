package org.example.plzdrawing.api.chatRoom.repository;

import static org.example.plzdrawing.common.redis.RedisKeyPrefix.*;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UnreadCountRepository {

    private final RedisTemplate<String, Integer> redisTemplate;

    public void incrementUnreadCount(String chatRoomId, Long memberId) {
        String key = getRedisKey(chatRoomId, memberId);
        redisTemplate.opsForValue().increment(key, 1);
    }

    public int getUnreadCount(String chatRoomId, Long memberId) {
        String key = getRedisKey(chatRoomId, memberId);
        return redisTemplate.opsForValue().get(key);
    }

    public void resetUnreadCount(String chatRoomId, Long memberId) {
        String key = getRedisKey(chatRoomId, memberId);
        redisTemplate.opsForValue().set(key, 0);
    }

    private String getRedisKey(String chatRoomId, Long memberId) {
        return CHATROOM_UNREAD + chatRoomId + ":" + memberId;
    }
}

