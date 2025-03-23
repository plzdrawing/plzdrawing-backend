package org.example.plzdrawing.api.auth.service.mail;

public interface MailService {

    void sendCodeEmail(String email);
    void sendEmailForRecoveryPassword(String email);
}
