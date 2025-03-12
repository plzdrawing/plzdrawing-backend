package org.example.plzdrawing.common.validator;

import org.example.plzdrawing.common.annotation.ValidPassword;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordValidator implements BaseValidator<ValidPassword> {
    // 영문 + 숫자 + 특수문자 혼합
    private static final String PASSWORD_REGEX = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]).*$";
    private static final String PASSWORD_REQUIRED_MESSAGE = "비밀번호를 작성해주세요.";
    private static final String LENGTH_ERROR_MESSAGE = "8~20자여야 합니다.";
    private static final String REGEX_ERROR_MESSAGE = "영문, 숫자, 특수문자를 포함하여 작성해주세요.";

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.trim().isEmpty()) {
            addConstraintViolationWithMessage(context, PASSWORD_REQUIRED_MESSAGE);
            return false;
        }

        if (value.length() < 8 || value.length() > 20) {
            addConstraintViolationWithMessage(context, LENGTH_ERROR_MESSAGE);
            return false;
        }

        if (!value.matches(PASSWORD_REGEX)) {
            addConstraintViolationWithMessage(context, REGEX_ERROR_MESSAGE);
            return false;
        }

        return true;
    }

}
