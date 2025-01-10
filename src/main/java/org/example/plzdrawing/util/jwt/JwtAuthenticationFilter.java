package org.example.plzdrawing.util.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.example.plzdrawing.api.auth.customuser.CustomUser;
import org.example.plzdrawing.api.auth.customuser.CustomUserService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final static String HEADER_AUTHORIZATION = "Authorization";
    private final static String TOKEN_PREFIX = "Bearer";

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserService customUserService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        String token = getAccessToken(request);
        if (token != null && jwtTokenProvider.validationToken(token)) {
            Authentication authentication = getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    private String getAccessToken(HttpServletRequest request) {
        String token = request.getHeader(HEADER_AUTHORIZATION);
        if (token == null) {
            return null;
        } else if (token.startsWith(TOKEN_PREFIX)) {
            return token.substring(7);
        }

        return null;
    }

    private Authentication getAuthentication(String token) {
        String memberId = jwtTokenProvider.getMemberId(token);
        CustomUser userDetails = customUserService.loadUserByUsername(memberId);

        return new UsernamePasswordAuthenticationToken(userDetails, "",
                userDetails.getAuthorities());
    }
}
