package org.example.plzdrawing.api.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import java.net.URI;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.plzdrawing.api.auth.customuser.CustomUser;
import org.example.plzdrawing.api.auth.dto.request.LoginRequest;
import org.example.plzdrawing.api.auth.dto.request.SignUpRequest;
import org.example.plzdrawing.api.auth.dto.response.LoginResponse;
import org.example.plzdrawing.api.auth.dto.response.ReissueResponse;
import org.example.plzdrawing.api.auth.dto.response.SignUpResponse;
import org.example.plzdrawing.api.auth.service.strategy.AuthService;
import org.example.plzdrawing.domain.Role;
import org.example.plzdrawing.domain.member.Provider;
import org.example.plzdrawing.util.jwt.TokenService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Tag(name = "인증 관련 컨트롤러", description = "AuthController")
public class AuthController {
    private final AuthStrategyManager strategyManager;
    private final TokenService tokenService;

    @PostMapping("/v1/signup")
    @Operation(summary = "회원가입", description = "signUp")
    public ResponseEntity<SignUpResponse> signUp(
            @AuthenticationPrincipal CustomUser customUser,
            @RequestBody @Valid SignUpRequest request,
            HttpServletResponse response) {
        AuthService authService = strategyManager.getAuthService(customUser.getProvider());
        authService.signUp(customUser, request);
        tokenService.createAccessToken(customUser.getMember().getId().toString(), Role.ROLE_MEMBER, response);
        URI location = ServletUriComponentsBuilder
                .fromPath("/api/member/{memberId}")
                .buildAndExpand(customUser.getMember().getId().toString())
                .toUri();
        return ResponseEntity.
                created(location).build();
    }

    @PostMapping("/v1/login")
    @Operation(summary = "로그인", description = "login")
    public ResponseEntity<Boolean> login(@RequestBody @Valid LoginRequest request, HttpServletResponse response) {
        AuthService authService = strategyManager.getAuthService(request.getProvider());

        return ResponseEntity.ok(authService.login(request, response));
    }

    @PostMapping("/v1/token/refresh")
    @Operation(summary = "토큰 재발급", description = "reissue")
    public ResponseEntity<Boolean> reissue(@RequestHeader("Authorization") String tokenHeader, HttpServletResponse response) {
        tokenService.reissue(tokenHeader, response);
        return ResponseEntity.ok(true);
    }
}