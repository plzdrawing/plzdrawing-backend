package org.example.plzdrawing.util.jwt;

import static org.example.plzdrawing.api.auth.exception.AuthErrorCode.MEMBER_NOT_EXIST;
import static org.example.plzdrawing.util.jwt.exception.JwtErrorCode.*;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.plzdrawing.common.exception.RestApiException;
import org.example.plzdrawing.domain.Role;
import org.example.plzdrawing.domain.member.MemberRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;
    private final RedisTemplate<String, String> redisTemplate;

    public void createAccessToken(String memberId, Role role, HttpServletResponse response) {
        String strRole = role.name();
        jwtTokenProvider.createAccessToken(memberId, strRole, response);
    }

    public void createRefreshToken(String memberId, HttpServletResponse response) {
        jwtTokenProvider.createRefreshToken(memberId, response);
    }

    public String reissue(String tokenHeader, HttpServletResponse response) {
        String refreshToken = removePrefix(tokenHeader);

        if (jwtTokenProvider.validationRefreshToken(refreshToken)) {
            Long memberId = Long.parseLong(jwtTokenProvider.getMemberId(refreshToken));
            Role role = memberRepository.findById(memberId)
                    .orElseThrow(() -> new RestApiException(MEMBER_NOT_EXIST.getErrorCode()))
                    .getRole();
            createAccessToken(memberId.toString(), role, response);
        }
        throw new RestApiException(TOKEN_INCORRECT.getErrorCode());
    }

    private String removePrefix(String tokenHeader) {
        if (!tokenHeader.startsWith("Bearer ")) {
            throw new RestApiException(INVALID_TOKEN.getErrorCode());
        }

        return tokenHeader.substring(7);
    }

    public void logout(String tokenHeader, HttpServletResponse response) {
        String accessToken = removePrefix(tokenHeader);

        if (!jwtTokenProvider.validationToken(accessToken)) {
            throw new RestApiException(INVALID_TOKEN.getErrorCode());
        }

        String memberId = jwtTokenProvider.getMemberId(accessToken);

        // Redis에서 RefreshToken 완전 삭제
        jwtTokenProvider.deleteRefreshToken(memberId);

        // 쿠키 초기화
        ResponseCookie accessCookie = ResponseCookie.from("access_token", "")
                .path("/")
                .maxAge(0)
                .httpOnly(true)
                .build();

        ResponseCookie refreshCookie = ResponseCookie.from("refresh_token", "")
                .path("/")
                .maxAge(0)
                .httpOnly(true)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());
    }
}
