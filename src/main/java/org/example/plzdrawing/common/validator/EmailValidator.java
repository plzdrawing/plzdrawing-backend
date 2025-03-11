package org.example.plzdrawing.common.validator;

import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;
import org.example.plzdrawing.common.annotation.ValidEmail;

@Slf4j
public class EmailValidator implements BaseValidator<ValidEmail> {
    private static final String EMAIL_REGEX = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    private static final String EMAIL_REQUIRED_MESSAGE = "메일을 작성해주세요.";
    private static final String REGEX_ERROR_MESSAGE = "이메일 형식이 올바르지 않습니다.";

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // Null 체크 필요! (null을 허용하면 검증이 무시됨)
        if (value == null || value.isBlank()) {
            addConstraintViolationWithMessage(context, EMAIL_REQUIRED_MESSAGE);
            return false;
        }

        if (!value.matches(EMAIL_REGEX)) {
            addConstraintViolationWithMessage(context, REGEX_ERROR_MESSAGE);
            return false;
        }

        return true;
    }
}