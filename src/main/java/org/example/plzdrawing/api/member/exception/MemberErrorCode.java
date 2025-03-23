package org.example.plzdrawing.api.member.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.plzdrawing.common.error.BaseErrorCode;
import org.example.plzdrawing.common.error.ErrorCode;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum MemberErrorCode {

    MEMBER_ALREADY_EXIST(new BaseErrorCode("MEMBER_001", HttpStatus.BAD_REQUEST, "이미 존재하는 회원입니다.")),
    MEMBER_NOT_FOUND(new BaseErrorCode("MEMBER_002", HttpStatus.NOT_FOUND, "존재하지 않는 회원입니다.")),
    PASSWORD_INCORRECT(new BaseErrorCode("MEMBER_003", HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다."))
    ;

    private final ErrorCode errorCode;
}
