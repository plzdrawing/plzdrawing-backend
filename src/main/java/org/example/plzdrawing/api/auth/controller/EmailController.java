package org.example.plzdrawing.api.auth.controller;

import lombok.RequiredArgsConstructor;
import org.example.plzdrawing.api.auth.dto.request.CodeGenerateRequest;
import org.example.plzdrawing.api.auth.dto.request.CodeGenerateForPasswordRequest;
import org.example.plzdrawing.api.auth.dto.request.PasswordResetRequest;
import org.example.plzdrawing.api.auth.dto.request.UpdatePasswordRequest;
import org.example.plzdrawing.api.auth.service.strategy.email.EmailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/email")
public class EmailController {

    private final EmailService emailService;

    @PostMapping("/v1/email-verification")
    public ResponseEntity<Void> sendEmailForVerification(@RequestBody CodeGenerateRequest request) {
        emailService.sendCode(request.getEmail());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/v1/email-verification")
    public ResponseEntity<Boolean> verifyEmail(@RequestParam("email") String email,
            @RequestParam("code") String authCode) {

        return ResponseEntity.ok(emailService.verifyAuthCode(email, authCode));
    }

    @PostMapping("/v1/password/reissue")
    public ResponseEntity<Void> sendEmailForReissuePassword(
            @RequestBody CodeGenerateForPasswordRequest request) {

        emailService.sendEmailForRecoveryPassword(request.getEmail());
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/v1/password/reissue")
    public ResponseEntity<Boolean> reissuePassword(@RequestBody PasswordResetRequest request) {

        return ResponseEntity.ok(emailService.reissuePassword(request.getEmail(), request.getAuthCode()));
    }

    @PatchMapping("/v1/password/update")
    public ResponseEntity<Void> updatePassword(@RequestBody UpdatePasswordRequest request) {

        emailService.updatePassword(request.getEmail(), request.getPassword());
        return ResponseEntity.ok().build();
    }
}