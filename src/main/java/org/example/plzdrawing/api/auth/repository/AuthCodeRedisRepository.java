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
    private final String REISSUE_PREFIX = "Reissue:";

    public void saveAuthNumber(String key, String emailAuthNumber) {
        redisTemplate.opsForValue()
                .set(PREFIX + key, emailAuthNumber, EXPIRATION, TimeUnit.MILLISECONDS);
    }
    public String findEmailAuthNumberByKey(String key) {
        return redisTemplate.opsForValue().get(PREFIX + key);
    }

    public void saveReissueAuthNumber(String key, String reissueAuthNumber) {
        redisTemplate.opsForValue().set(REISSUE_PREFIX + key, reissueAuthNumber, EXPIRATION, TimeUnit.MILLISECONDS);
    }
    public String findReissueAuthNumberByKey(String key) {
        return redisTemplate.opsForValue().get(REISSUE_PREFIX + key);
    }
}
