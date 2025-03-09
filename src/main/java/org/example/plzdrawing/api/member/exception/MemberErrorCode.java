package org.example.plzdrawing.api.member.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.plzdrawing.common.error.ErrorCode;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum MemberErrorCode implements ErrorCode {//1

    MEMBER_ALREADY_EXIST(4001, HttpStatus.BAD_REQUEST, "이미 존재하는 회원입니다."),
    MEMBER_NOT_FOUND(4041, HttpStatus.NOT_FOUND, "존재하지 않는 회원입니다."),
    PASSWORD_INCORRECT(4011, HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다.")
    ;

    private final int code;
    private final HttpStatus httpStatus;
    private final String message;
}
