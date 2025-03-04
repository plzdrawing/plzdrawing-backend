package org.example.plzdrawing.api.auth.repository;

import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RefreshTokenRedisRepository {

    @Value("${jwt.refresh-expiration}")
    private Long EXPIRATION;

    private final StringRedisTemplate redisTemplate;

    public void saveRefreshToken(String memberId, String jti, String refreshToken) {
        String redisId = createRedisId(memberId, jti);
        redisTemplate.opsForValue().set(redisId, refreshToken, EXPIRATION, TimeUnit.MILLISECONDS);
    }


    public String findRefreshToken(String memberId, String jti) {
        String redisId = createRedisId(memberId, jti);
        return redisTemplate.opsForValue().get(redisId);
    }

    private String createRedisId(String memberId, String jti) {
        return memberId + ":" + jti;
    }
}
