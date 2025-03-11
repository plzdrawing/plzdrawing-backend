package org.example.plzdrawing.common.error;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.FieldError;

@Builder
@RequiredArgsConstructor
@Getter
public class ValidationError {
    private final String field;
    private final String message;
    private final String rejectedValue;

    public static ValidationError of(final FieldError fieldError) {
        return ValidationError.builder()
                .field(fieldError.getField())
                .message(fieldError.getDefaultMessage())
                .rejectedValue(String.valueOf(fieldError.getRejectedValue()))
                .build();
    }
}
