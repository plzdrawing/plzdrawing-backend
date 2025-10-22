package org.example.plzdrawing.util.jwt;

import static org.example.plzdrawing.util.jwt.exception.JwtErrorCode.*;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.Date;
import java.util.UUID;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.plzdrawing.api.auth.repository.RefreshTokenRedisRepository;
import org.example.plzdrawing.common.cookie.CookieService;
import org.example.plzdrawing.common.exception.RestApiException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String SECRET_KEY;

    @Value("${jwt.access-expiration}")
    private Long EXPIRATION_TIME;

    @Value("${jwt.refresh-expiration}")
    private Long REFRESH_EXPIRATION_TIME;

    private final RefreshTokenRedisRepository refreshTokenRedisRepository;
    private final CookieService cookieService;

    public void createAccessToken(String memberId, String role, HttpServletResponse response) {
        System.out.println("쿠키생성");
        String accessToken =  Jwts.builder()
                .subject(memberId)
                .claim("role", role)
                .signWith(getSigningKey())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .compact();
        ResponseCookie cookie = cookieService.createCookie("access_token", accessToken, EXPIRATION_TIME);
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        System.out.println(accessToken);

    }

    public void createRefreshToken(String memberId, HttpServletResponse response) {
        String jti = createUUID();
        String refreshToken =  Jwts.builder()
                .subject(memberId)
                .id(jti)
                .signWith(getSigningKey())
                .expiration(new Date(System.currentTimeMillis() + REFRESH_EXPIRATION_TIME))
                .compact();

        ResponseCookie cookie = cookieService.createCookie("refresh_token", refreshToken, REFRESH_EXPIRATION_TIME);
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        refreshTokenRedisRepository.saveRefreshToken(String.valueOf(memberId), jti, refreshToken);
    }

    public boolean validationToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (ExpiredJwtException e) {
            throw new RestApiException(TOKEN_EXPIRED.getErrorCode());
        } catch (JwtException e) {
            throw new RestApiException(INVALID_TOKEN.getErrorCode());
        }
    }

    public boolean validationRefreshToken(String refreshToken) {
        try {
            Jws<Claims> claims = Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(refreshToken);

            String memberId = claims.getPayload().getSubject();
            String jti = claims.getPayload().getId();

            String storedToken = refreshTokenRedisRepository.findRefreshToken(memberId, jti);

            return storedToken != null && storedToken.equals(refreshToken);
        } catch (ExpiredJwtException e) {
            throw new RestApiException(TOKEN_EXPIRED.getErrorCode());
        } catch (JwtException e) {
            throw new RestApiException(INVALID_TOKEN.getErrorCode());
        }
    }

    //TODO 회원탈퇴
    public void deleteRefreshToken(String redisId) {

    }

    public String getMemberId(String token) {
        Jws<Claims> claims = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token);
        return claims.getPayload().getSubject();
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private String createUUID() {
        return UUID.randomUUID().toString();
    }
}
