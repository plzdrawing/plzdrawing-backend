package org.example.plzdrawing.common.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.annotation.Annotation;

public interface BaseValidator<T extends Annotation> extends ConstraintValidator<T, String> {
    default void addConstraintViolationWithMessage(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
                .addConstraintViolation();
    }

    @Override
    boolean isValid(String value, ConstraintValidatorContext context);

}
