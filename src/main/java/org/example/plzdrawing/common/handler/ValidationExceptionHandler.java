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

    // requestParam 검증 시 발생되는 예외 처리
    @ExceptionHandler(HandlerMethodValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleHandlerMethodValidationException(HandlerMethodValidationException ex) {
        log.info("handleHandlerMethodValidationException");

        List<ValidationError> errors = ex.getParameterValidationResults().stream()
                .flatMap(result -> result.getResolvableErrors().stream()
                .map(error -> new ValidationError(
                        result.getMethodParameter().getParameterName(),
                        error.getDefaultMessage(),
                        result.getArgument().toString())
                )).toList();

        return makeErrorResponse(errors);
    }

    private ErrorResponse makeErrorResponse(BindingResult bindingResult) {
        List<ValidationError> errors = bindingResult.getFieldErrors()
                                        .stream().map(ValidationError::of).toList();

        return new ErrorResponse(CommonErrorCode.INVALID_FIELD, errors);
    }

    private ErrorResponse makeErrorResponse(List<ValidationError> errors) {

        return new ErrorResponse(CommonErrorCode.INVALID_FIELD, errors);
    }

    // TODO : not yet 1
    /*private static final HashMap<String, String> messageTypeMismatchException = new HashMap<>();
    static {
        messageTypeMismatchException.put("date", "date의 format은 yyyy-MM-dd 입니다.");
    }
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public BaseResponse<?> handleTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        log.info("MethodArgumentTypeMismatchException");

        String message = messageTypeMismatchException.get(ex.getName());
        if(message == null) message = ex.getMessage();

        return new BaseResponse<>(CommonResponseStatus.METHOD_ARGUMENT_TYPE_MISMATCH,
                new ValidationError(ex.getName(), message, String.valueOf(ex.getValue())));
    }*/


    // TODO : not yet 2
   /* @ExceptionHandler(InvalidFormatException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BaseResponse<?> handleHttpMessageNotReadableException(InvalidFormatException ex) {
        log.info("InvalidFormatException");
        ErrorsResponse build = ErrorsResponse.builder()
                .errors(
                        Collections.singletonList(new ValidationError(
                                extractFieldName(ex.getPath().toString()),
                                ex.getOriginalMessage(),
                                (String) ex.getValue()))).build();

        return new BaseResponse<>(CommonResponseStatus.INVALID_FIELD, build);
    }

    private static String extractFieldName(String path) {
        int startIndex = path.lastIndexOf("[\"");
        int endIndex = path.lastIndexOf("\"]");
        if (startIndex != -1 && endIndex != -1) {
            return path.substring(startIndex + 2, endIndex);
        }
        return path;
    }*/
}
