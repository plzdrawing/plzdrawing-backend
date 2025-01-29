package org.example.plzdrawing.util.random;

import java.security.SecureRandom;
import org.springframework.stereotype.Component;

@Component
public class RandomGenerator {

    private static final SecureRandom secureRandom = new SecureRandom();

    public String makeSecureRandomNumber() {
        return String.format("%06d", secureRandom.nextInt(1000000));
    }

    public String generateTemporaryPassword() {
        String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
        String DIGITS = "0123456789";
        String SPECIAL_CHARACTERS = "!@#$%^&*()-_=+[]{}|;:',.<>?/~`";
        String ALL_CHARACTERS = UPPERCASE + LOWERCASE + DIGITS + SPECIAL_CHARACTERS;
        int DEFAULT_LENGTH = 12;

        StringBuilder password = new StringBuilder();
        password.append(UPPERCASE.charAt(secureRandom.nextInt(UPPERCASE.length())));
        password.append(LOWERCASE.charAt(secureRandom.nextInt(LOWERCASE.length())));
        password.append(DIGITS.charAt(secureRandom.nextInt(DIGITS.length())));
        password.append(SPECIAL_CHARACTERS.charAt(secureRandom.nextInt(SPECIAL_CHARACTERS.length())));

        for (int i = 4; i < DEFAULT_LENGTH; i++) {
            password.append(ALL_CHARACTERS.charAt(secureRandom.nextInt(ALL_CHARACTERS.length())));
        }

        return shufflePassword(password.toString());
    }

    private String shufflePassword(String password) {
        char[] characters = password.toCharArray();
        for (int i = characters.length - 1; i > 0; i--) {
            int j = secureRandom.nextInt(i + 1);
            char temp = characters[i];
            characters[i] = characters[j];
            characters[j] = temp;
        }
        return new String(characters);
    }
}
