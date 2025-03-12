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

    public static ValidationError of(final FieldError fieldError) {
        return ValidationError.builder()
                .field(fieldError.getField())
                .message(fieldError.getDefaultMessage())
                .build();
    }
}
