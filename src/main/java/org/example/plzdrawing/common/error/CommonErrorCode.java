package org.example.plzdrawing.common.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CommonErrorCode{
    // 4000 : 요청 실패
    INVALID_FIELD(new BaseErrorCode(2010, HttpStatus.BAD_REQUEST, "요청 값이 잘못되었습니다.")),
    BAD_REQUEST(new BaseErrorCode(4000, HttpStatus.BAD_REQUEST, "잘못된 파라미터입니다.")),

    // RequestParam exception
    EMPTY_REQUEST_PARAMETER(new BaseErrorCode(4001, HttpStatus.METHOD_NOT_ALLOWED,"Request Parameter가 존재하지 않습니다.")),
    METHOD_ARGUMENT_TYPE_MISMATCH(new BaseErrorCode(4002, HttpStatus.METHOD_NOT_ALLOWED, "Request Parameter나 Path Variable의 유형이 불일치합니다."))
    ;

    private final ErrorCode errorCode;
}
