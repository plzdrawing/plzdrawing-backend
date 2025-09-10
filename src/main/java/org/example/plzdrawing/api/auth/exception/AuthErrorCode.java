package org.example.plzdrawing.api.auth.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.plzdrawing.common.error.BaseErrorCode;
import org.example.plzdrawing.common.error.ErrorCode;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AuthErrorCode{
    AUTH_CODE_INCORRECT(new BaseErrorCode("AUTH_001", HttpStatus.UNAUTHORIZED, "인증번호가 일치하지 않습니다.")),
    MEMBER_NOT_EXIST(new BaseErrorCode("AUTH_002", HttpStatus.NOT_FOUND, "해당 멤버를 찾을 수 없습니다.")),
    EXIST_EMAIL(new BaseErrorCode("AUTH_003", HttpStatus.BAD_REQUEST, "이미 존재하는 이메일 입니다."));
    private final ErrorCode errorCode;
}
