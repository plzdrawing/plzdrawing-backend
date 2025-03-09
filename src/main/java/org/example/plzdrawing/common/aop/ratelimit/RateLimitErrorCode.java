package org.example.plzdrawing.common.aop.ratelimit;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.plzdrawing.common.error.ErrorCode;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum RateLimitErrorCode implements ErrorCode {//3

    EXCEED_REQUEST_COUNT(4293, HttpStatus.TOO_MANY_REQUESTS, "요청 횟수를 초과하였습니다.")
    ;
    private final int code;
    private final HttpStatus httpStatus;
    private final String message;
}
