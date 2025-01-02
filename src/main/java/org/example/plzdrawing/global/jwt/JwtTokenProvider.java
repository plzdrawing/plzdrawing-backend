package org.example.plzdrawing.global.jwt;

import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class JwtTokenProvider {

    private static String SECRET_KEY;
    private static long EXPIRATION_TIME;
    private static long REFRESH_EXPIRATION_TIME;


    @Value("${jwt.secret}")
    public void setSecretKey(String secretKey) {
        SECRET_KEY = secretKey;
    }

    @Value("${jwt.access-expiration}")
    public void setExpirationTime(long expirationTime) {
        EXPIRATION_TIME = expirationTime;
    }

    @Value("${jwt.refresh-expiration}")
    public void setRefreshExpirationTime(long refreshExpirationTime) {
        REFRESH_EXPIRATION_TIME = refreshExpirationTime;
    }

    //TODO 토큰 클레임 미정

    public String createAccessToken() {

    }

    public String createRefreshToken() {

    }

    public boolean validationToken(String token) {

    }

    public boolean validationRefreshToken(String token) {

    }

    public void deleteRefreshToken(String redisId) {

    }

    public String createRedisId() {

    }

    private SecretKey getSigningKey() {

    }
}
