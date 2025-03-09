package org.example.plzdrawing.util.jwt.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.plzdrawing.common.error.ErrorCode;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum JwtErrorCode implements ErrorCode {//0

    TOKEN_EXPIRED(4010, HttpStatus.UNAUTHORIZED, "만료된 토큰입니다."),
    INVALID_TOKEN(4010, HttpStatus.UNAUTHORIZED, "토큰 형식이 올바르지 않습니다."),
    TOKEN_INCORRECT(4010, HttpStatus.UNAUTHORIZED, "토큰이 일치하지 않습니다.")
    ;

    private final int code;
    private final HttpStatus httpStatus;
    private final String message;
}
