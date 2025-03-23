package org.example.plzdrawing.api.auth.repository;

import static org.example.plzdrawing.common.redis.RedisKeyPrefix.EMAIL_AUTH_NUMBER;

import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AuthCodeRedisRepository {

    @Value("${spring.mail.auth-code-expiration}")
    private Long EXPIRATION;

    private final RedisTemplate<String, String> redisTemplate;


    public void saveAuthNumber(String key, String emailAuthNumber) {
        redisTemplate.opsForValue()
                .set(EMAIL_AUTH_NUMBER + key, emailAuthNumber, EXPIRATION, TimeUnit.MILLISECONDS);
    }

    public String findEmailAuthNumberByKey(String key) {
        return redisTemplate.opsForValue().get(EMAIL_AUTH_NUMBER + key);
    }
}
