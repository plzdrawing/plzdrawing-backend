package org.example.plzdrawing.util.jwt;

import static org.example.plzdrawing.util.jwt.exception.JwtErrorCode.*;

import lombok.RequiredArgsConstructor;
import org.example.plzdrawing.common.exception.RestApiException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final JwtTokenProvider jwtTokenProvider;
    public String createAccessToken(Long memberId) {
        return jwtTokenProvider.createAccessToken(String.valueOf(memberId));
    }

    public String createRefreshToken(Long memberId) {
        return jwtTokenProvider.createRefreshToken(String.valueOf(memberId));
    }

    public String reissue(String tokenHeader) {
        String refreshToken = removePrefix(tokenHeader);
        if (jwtTokenProvider.validationRefreshToken(refreshToken)) {
            return createAccessToken(Long.parseLong(jwtTokenProvider.getMemberId(refreshToken)));
        }
        throw new RestApiException(TOKEN_INCORRECT.getErrorCode());
    }

    private String removePrefix(String tokenHeader) {
        if (!tokenHeader.startsWith("Bearer ")) {
            throw new RestApiException(INVALID_TOKEN.getErrorCode());
        }

        return tokenHeader.substring(7);
    }
}
