package org.example.plzdrawing.util.jwt.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.plzdrawing.common.error.BaseErrorCode;
import org.example.plzdrawing.common.error.ErrorCode;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum JwtErrorCode {

    TOKEN_EXPIRED(new BaseErrorCode("JWT_001", HttpStatus.UNAUTHORIZED, "만료된 토큰입니다.")),
    INVALID_TOKEN(new BaseErrorCode("JWT_002", HttpStatus.UNAUTHORIZED, "토큰 형식이 올바르지 않습니다.")),
    TOKEN_INCORRECT(new BaseErrorCode("JWT_003", HttpStatus.UNAUTHORIZED, "토큰이 일치하지 않습니다."))
    ;
    private final ErrorCode errorCode;

}
