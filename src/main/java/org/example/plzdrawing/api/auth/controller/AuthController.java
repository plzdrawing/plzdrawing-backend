package org.example.plzdrawing.api.auth.controller;

import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.example.plzdrawing.api.auth.dto.request.EmailVerificationRequest;
import org.example.plzdrawing.api.auth.dto.request.LoginRequest;
import org.example.plzdrawing.api.auth.dto.request.SignUpRequest;
import org.example.plzdrawing.api.auth.dto.response.EmailVerificationResponse;
import org.example.plzdrawing.api.auth.dto.response.LoginResponse;
import org.example.plzdrawing.api.auth.dto.response.ReissueResponse;
import org.example.plzdrawing.api.auth.dto.response.SignUpResponse;
import org.example.plzdrawing.api.auth.service.mail.MailService;
import org.example.plzdrawing.api.auth.service.strategy.AuthService;
import org.example.plzdrawing.util.jwt.TokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthStrategyManager strategyManager;
    private final MailService mailService;
    private final TokenService tokenService;

    @PostMapping("/v1/email-verification")
    public ResponseEntity<EmailVerificationResponse> sendEmailForVerification(
            @RequestBody EmailVerificationRequest request) {

        return ResponseEntity.ok(mailService.sendCodeEmail(request.getEmail()));
    }

    @GetMapping("/v1/email-verification")
    public ResponseEntity<Boolean> verifyEmail(@RequestParam("email") String email,
            @RequestParam("code") String authCode) {

        return ResponseEntity.ok(mailService.verifyAuthCode(email, authCode));
    }


    @PostMapping("/v1/signup")
    public ResponseEntity<SignUpResponse> signUp(@RequestBody SignUpRequest request) {
        AuthService authService = strategyManager.getAuthService(request.getProvider());

        Long savedId = authService.signUp(request).getMemberId();
        URI location = ServletUriComponentsBuilder
                .fromPath("/api/member/{memberId}")
                .buildAndExpand(savedId)
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @PostMapping("/v1/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        AuthService authService = strategyManager.getAuthService(request.getProvider());

        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/v1/token/refresh")
    public ResponseEntity<ReissueResponse> reissue(@RequestHeader("Authorization") String tokenHeader) {
        ReissueResponse response = new ReissueResponse(tokenService.reissue(tokenHeader));

        return ResponseEntity.ok(response);
    }
}