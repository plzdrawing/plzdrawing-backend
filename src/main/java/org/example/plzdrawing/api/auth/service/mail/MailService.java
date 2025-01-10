package org.example.plzdrawing.api.auth.service.mail;

import org.example.plzdrawing.api.auth.dto.response.EmailVerificationResponse;

public interface MailService {

    EmailVerificationResponse sendCodeEmail(String email);
    boolean verifyAuthCode(String email, String code);
}
