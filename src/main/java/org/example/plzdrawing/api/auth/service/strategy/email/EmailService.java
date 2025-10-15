package org.example.plzdrawing.api.auth.service.strategy.email;

import jakarta.servlet.http.HttpServletResponse;
import org.example.plzdrawing.api.auth.service.strategy.AuthService;

public interface EmailService extends AuthService {

    void sendCode(String email);
    void verifyAuthCode(String email, String authCode, HttpServletResponse response);
    void sendEmailForRecoveryPassword(String email);
    Boolean reissuePassword(String email, String authCode);
    void changePassword(String email, String nowPassword, String newPassword);
}
