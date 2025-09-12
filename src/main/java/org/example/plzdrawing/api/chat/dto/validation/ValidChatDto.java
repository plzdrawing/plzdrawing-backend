package org.example.plzdrawing.api.chat.dto.validation;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = ChatDtoValidator.class)
@Target({ElementType.TYPE})
@Retention(RUNTIME)
public @interface ValidChatDto {
    String message() default "잘못된 요청입니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}