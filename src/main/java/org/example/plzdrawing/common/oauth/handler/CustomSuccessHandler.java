package org.example.plzdrawing.common.oauth.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.example.plzdrawing.common.oauth.CustomOAuth2User;
import org.example.plzdrawing.domain.member.Member;
import org.example.plzdrawing.domain.member.MemberRepository;
import org.example.plzdrawing.util.jwt.TokenService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final TokenService tokenService;
    private final MemberRepository memberRepository;

    @Value("${member-redirect-url}")
    private String memberRedirectUrl;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {

        Object principal = authentication.getPrincipal();

        if (!(principal instanceof CustomOAuth2User)) {
            throw new BadRequestException("sarangggun.oauth.invalid-principal");
        }

        CustomOAuth2User customUser = (CustomOAuth2User) principal;

        String accessToken;
        String refreshToken = tokenService.createRefreshToken(customUser.getUserId().toString());

        Optional<Member> optionalMember = getMemberByEmail(customUser.getEmail());

        if (optionalMember.isPresent()) {
            Member member = optionalMember.get();
            accessToken = tokenService.createAccessToken(member.getId().toString(), member.getRole());
        } else {
            accessToken = tokenService.createAccessToken(customUser.getUserId().toString(), customUser.getRole());
        }

        response.addHeader("access_token", "Bearer " + accessToken);
        response.addHeader("refresh_token", "Bearer " + refreshToken);
        response.sendRedirect(memberRedirectUrl);
    }

    private Optional<Member> getMemberByEmail(String email) {
        return memberRepository.findByEmail(email);
    }
}
