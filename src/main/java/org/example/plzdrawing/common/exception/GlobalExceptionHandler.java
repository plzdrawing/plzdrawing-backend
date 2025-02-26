package org.example.plzdrawing.common.exception;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import lombok.NonNull;
import org.example.plzdrawing.api.chatRoom.exception.ChatRoomAlreadyExistsException;
import org.example.plzdrawing.common.error.CommonErrorCode;
import org.example.plzdrawing.common.error.ErrorResponse;
import org.example.plzdrawing.common.error.ErrorResponse.ValidationError;
import org.example.plzdrawing.common.error.ErrorCode;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ChatRoomAlreadyExistsException.class)
    public ResponseEntity<Void> handleChatRoomAlreadyExists(ChatRoomAlreadyExistsException ex) {
        String chatRoomId = ex.getChatRoom().getChatRoomId();
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/api/v1/chat/"+chatRoomId));
        return new ResponseEntity<>(headers, HttpStatus.SEE_OTHER);
    }

    @ExceptionHandler(RestApiException.class)
    public ResponseEntity<Object> handleCustomException(RestApiException e) {
        ErrorCode errorCode = e.getErrorCode();
        return handleExceptionInternal(errorCode);
    }

    private ResponseEntity<Object> handleExceptionInternal(ErrorCode errorCode) {
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(makeErrorResponse(errorCode));
    }

    private ErrorResponse makeErrorResponse(ErrorCode errorCode) {
        return ErrorResponse.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .build();
    }

    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(
            @NonNull MethodArgumentNotValidException e,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode status,
            @NonNull WebRequest request
    ) {
        ErrorCode errorCode = CommonErrorCode.BAD_REQUEST.getErrorCode();
        return handleExceptionInternal(e, errorCode);
    }

    private ResponseEntity<Object> handleExceptionInternal(BindException e, ErrorCode errorCode) {
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(makeErrorResponse(e, errorCode));
    }

    private ErrorResponse makeErrorResponse(BindException e, ErrorCode errorCode) {
        List<ValidationError> validationErrorList = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(ErrorResponse.ValidationError::of)
                .collect(Collectors.toList());

        return ErrorResponse.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .errors(validationErrorList)
                .build();
    }
}

