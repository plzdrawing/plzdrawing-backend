package org.example.plzdrawing.api.auth.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.plzdrawing.common.error.BaseErrorCode;
import org.example.plzdrawing.common.error.ErrorCode;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AuthErrorCode{//2
    AUTH_CODE_INCORRECT(new BaseErrorCode(4012, HttpStatus.UNAUTHORIZED, "인증번호가 일치하지 않습니다."))
    ;
    private final ErrorCode errorCode;
}
