package org.example.plzdrawing.util.jwt;

import static org.example.plzdrawing.api.auth.exception.AuthErrorCode.MEMBER_NOT_EXIST;
import static org.example.plzdrawing.util.jwt.exception.JwtErrorCode.*;

import lombok.RequiredArgsConstructor;
import org.example.plzdrawing.common.exception.RestApiException;
import org.example.plzdrawing.domain.Role;
import org.example.plzdrawing.domain.member.MemberRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;
    public void createAccessToken(String memberId, Role role) {
        String strRole = role.name();
        jwtTokenProvider.createAccessToken(memberId, strRole);
    }

    public void createRefreshToken(String memberId) {
        jwtTokenProvider.createRefreshToken(memberId);
    }

    public String reissue(String tokenHeader) {
        String refreshToken = removePrefix(tokenHeader);

        if (jwtTokenProvider.validationRefreshToken(refreshToken)) {
            Long memberId = Long.parseLong(jwtTokenProvider.getMemberId(refreshToken));
            Role role = memberRepository.findById(memberId)
                    .orElseThrow(() -> new RestApiException(MEMBER_NOT_EXIST.getErrorCode()))
                    .getRole();
            createAccessToken(memberId.toString(), role);
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
