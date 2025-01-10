package org.example.plzdrawing.api.auth.service.mail;

import java.security.SecureRandom;
import org.springframework.stereotype.Component;

@Component
public class RandomGenerator {

    private static final SecureRandom secureRandom = new SecureRandom();

    public String makeSecureRandomNumber() {
        return String.format("%06d", secureRandom.nextInt(1000000));
    }
}
