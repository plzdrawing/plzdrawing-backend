package org.example.plzdrawing.common.aop.ratelimit;

import static org.example.plzdrawing.common.aop.ratelimit.RateLimitErrorCode.*;

import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.example.plzdrawing.common.exception.RestApiException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class RateLimitAspect {

    private final StringRedisTemplate redisTemplate;

    @Around(value = "@annotation(rateLimit) && args(email,..)")
    public Object checkRateLimit(ProceedingJoinPoint pjp, RateLimit rateLimit, String email) throws Throwable {
        RateLimitFeature feature = rateLimit.feature();
        int maxCount = rateLimit.maxCount();
        int timeWindow = rateLimit.timeWindow();

        String redisKey = buildRateLimitKey(email, feature);

        String currentCountStr = redisTemplate.opsForValue().get(redisKey);
        int currentCount = currentCountStr == null ? 0 : Integer.parseInt(currentCountStr);

        if (currentCount >= maxCount) {
            throw new RestApiException(EXCEED_REQUEST_COUNT.getErrorCode());
        }

        redisTemplate.opsForValue().increment(redisKey);
        Long expire = redisTemplate.getExpire(redisKey, TimeUnit.MILLISECONDS);
        if (expire < 0) {
            redisTemplate.expire(redisKey, timeWindow, TimeUnit.MILLISECONDS);
        }

        return pjp.proceed();
    }

    private String buildRateLimitKey(String email, RateLimitFeature feature) {
        return "EMAIL_VERIFICATION:" + email + ":" + feature.name();
    }
}
