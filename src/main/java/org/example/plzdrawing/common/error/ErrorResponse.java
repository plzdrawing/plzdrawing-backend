package org.example.plzdrawing.common.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@Getter
@Builder
@RequiredArgsConstructor
@JsonPropertyOrder({"code", "message", "errors"})
public class ErrorResponse {

    private final String code;
    private final String message;

    @JsonInclude(Include.NON_EMPTY)
    private final List<ValidationError> errors;

    public ErrorResponse(ErrorCode errorCode, List<ValidationError> errors) {
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
        this.errors = errors;
    }

    public ErrorResponse(ErrorCode errorCode) {
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
        this.errors = null;
    }
}
