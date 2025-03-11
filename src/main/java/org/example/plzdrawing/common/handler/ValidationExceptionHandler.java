package org.example.plzdrawing.common.handler;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.example.plzdrawing.common.error.CommonErrorCode;
import org.example.plzdrawing.common.error.ErrorResponse;
import org.example.plzdrawing.common.error.ValidationError;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.util.List;

@Slf4j
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ValidationExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodArgumentNotValidException(
            @NonNull MethodArgumentNotValidException ex){
        log.info("handleMethodArgumentNotValidException");
        BindingResult bindingResult = ex.getBindingResult();
        return makeErrorResponse(bindingResult);
    }

    private ErrorResponse makeErrorResponse(BindingResult bindingResult) {
        List<ValidationError> errors = bindingResult.getFieldErrors()
                                        .stream().map(ValidationError::of).toList();

        return new ErrorResponse(CommonErrorCode.INVALID_FIELD, errors);
    }

    private ErrorResponse makeErrorResponse(List<ValidationError> errors) {

        return new ErrorResponse(CommonErrorCode.INVALID_FIELD, errors);
    }

}
