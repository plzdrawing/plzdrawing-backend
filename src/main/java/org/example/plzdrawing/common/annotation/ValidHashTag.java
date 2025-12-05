package org.example.plzdrawing.common.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.example.plzdrawing.common.validator.HashTagValidator;

@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = HashTagValidator.class)
public @interface ValidHashTag {
    String message() default "해시태그 형식이 올바르지 않습니다.";

    int maxCount() default 5;
    int maxLength() default 10;

    // 정규식: 한글,영문,숫자만 허용 (특수문자 금지)
    String allowedPattern() default "^[a-zA-Z0-9가-힣]+$";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
