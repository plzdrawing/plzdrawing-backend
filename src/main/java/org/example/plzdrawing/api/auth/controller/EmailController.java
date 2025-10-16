package org.example.plzdrawing.api.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.plzdrawing.api.auth.dto.request.CodeGenerateForPasswordRequest;
import org.example.plzdrawing.api.auth.dto.request.CodeGenerateRequest;
import org.example.plzdrawing.api.auth.dto.request.PasswordResetRequest;
import org.example.plzdrawing.api.auth.dto.request.UpdatePasswordRequest;
import org.example.plzdrawing.api.auth.service.strategy.email.EmailService;
import org.example.plzdrawing.common.annotation.ValidEmail;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/email")
@Tag(name = "이메일 관련 컨트롤러", description = "EmailController")
public class EmailController {

    private final EmailService emailService;

    @PostMapping("/v1/email-verification")
    @Operation(summary = "이메일 코드 보내기", description = "sendEmailForVerification")
    public ResponseEntity<Void> sendEmailForVerification(@RequestBody @Valid CodeGenerateRequest request) {
        emailService.sendCode(request.getEmail());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/v1/email-verification")
    @Operation(summary = "이메일 인증", description = "verifyEmail")
    public ResponseEntity<Boolean> verifyEmail(@RequestParam("email") @ValidEmail String email,
            @RequestParam("code") String authCode,
            HttpServletResponse response) {
        emailService.verifyAuthCode(email, authCode, response);
        return ResponseEntity.ok()
                .body(true);
    }

    @PostMapping("/v1/password/reissue")
    @Operation(summary = "비밀번호 재발급 인증번호 전송", description = "verifyEmail")
    public ResponseEntity<Void> sendEmailForReissuePassword(
            @RequestBody CodeGenerateForPasswordRequest request) {

        emailService.sendEmailForRecoveryPassword(request.getEmail());
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/v1/password/reissue")
    @Operation(summary = "비밀번호 재발급", description = "reissuePassword")
    public ResponseEntity<Boolean> reissuePassword(@RequestBody @Valid PasswordResetRequest request) {

        return ResponseEntity.ok(emailService.reissuePassword(request.getEmail(), request.getAuthCode()));
    }

    @PatchMapping("/v1/password/update")
    @Operation(summary = "비밀번호 변경", description = "updatePassword")
    public ResponseEntity<Void> updatePassword(@RequestBody @Valid UpdatePasswordRequest request) {

        emailService.changePassword(request.getEmail(), request.getNowPassword(), request.getNewPassword());
        return ResponseEntity.ok().build();
    }
}