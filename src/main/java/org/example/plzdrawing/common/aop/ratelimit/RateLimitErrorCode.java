package org.example.plzdrawing.common.aop.ratelimit;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.plzdrawing.common.error.BaseErrorCode;
import org.example.plzdrawing.common.error.ErrorCode;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum RateLimitErrorCode {

    EXCEED_REQUEST_COUNT(new BaseErrorCode("RATELIMIT_001", HttpStatus.TOO_MANY_REQUESTS, "요청 횟수를 초과하였습니다."))
    ;
    private final ErrorCode errorCode;
}
