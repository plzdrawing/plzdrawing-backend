package org.example.plzdrawing.common.handler;

import lombok.extern.slf4j.Slf4j;
import org.example.plzdrawing.common.error.ErrorResponse;
import org.example.plzdrawing.common.exception.RestApiException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(RestApiException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleCustomException(RestApiException e) {
        log.info("handleCustomException");
        return new ErrorResponse(e.getErrorCode());
    }
}

