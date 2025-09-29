package org.example.plzdrawing.api.auth.service.mail;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class SendMailService {

    @Value("${spring.mail.username}")
    private String fromAddress;

    private final JavaMailSender emailSender;

    public void sendEmail(String to, String subject, String content) {
        MimeMessagePreparator messagePreparer = mimeMessage -> {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setFrom(fromAddress);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content);
        };
        emailSender.send(messagePreparer);
    }
}
