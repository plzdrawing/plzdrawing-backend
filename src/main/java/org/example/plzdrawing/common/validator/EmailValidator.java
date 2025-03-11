package org.example.plzdrawing.common.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;
import org.example.plzdrawing.common.annotation.ValidEmail;

@Slf4j
public class EmailValidator implements ConstraintValidator<ValidEmail, String> {
    private static final String EMAIL_REGEX = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // Null 체크 필요! (null을 허용하면 검증이 무시됨)
        if (value == null || value.isBlank()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("메일을 작성해주세요.")
                    .addConstraintViolation();
            log.info("EmailValidator - isValid : false(null)");
            return false;
        }

        if (!value.matches(EMAIL_REGEX)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("이메일 형식이 올바르지 않습니다.")
                    .addConstraintViolation();
            log.info("EmailValidator - isValid : false(match)");

            return false;
        }

        log.info("EmailValidator - isValid : true");
        return true;
    }
}