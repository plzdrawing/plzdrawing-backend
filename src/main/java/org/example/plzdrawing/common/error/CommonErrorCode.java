package org.example.plzdrawing.common.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CommonErrorCode {

    BAD_REQUEST(new BaseErrorCode(4000, HttpStatus.BAD_REQUEST, "잘못된 파라미터입니다."));

    private final ErrorCode errorCode;
}
