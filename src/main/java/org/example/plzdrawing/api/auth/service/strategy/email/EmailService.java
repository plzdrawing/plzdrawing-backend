package org.example.plzdrawing.api.auth.service.strategy.email;

import org.example.plzdrawing.api.auth.service.strategy.AuthService;

public interface EmailService extends AuthService {

    void sendCode(String email);
    Boolean verifyAuthCode(String email, String authCode);
    void sendEmailForRecoveryPassword(String email);
    Boolean reissuePassword(String email, String authCode);
    void changePassword(String email, String nowPassword, String newPassword);
}
