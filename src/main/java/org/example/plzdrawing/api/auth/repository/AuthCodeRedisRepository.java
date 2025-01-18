package org.example.plzdrawing.api.auth.repository;

import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthCodeRedisRepository {

    @Value("${spring.mail.auth-code-expiration}")
    private Long EXPIRATION;

    private final StringRedisTemplate redisTemplate;
    private final String PREFIX = "AuthNumber:";


    public void saveAuthNumber(String key, String emailAuthNumber) {
        redisTemplate.opsForValue().set(PREFIX+key, emailAuthNumber, EXPIRATION, TimeUnit.MILLISECONDS);
    }

    public String findEmailAuthNumberByKey(String key) {
        return redisTemplate.opsForValue().get(PREFIX + key);
    }
}
