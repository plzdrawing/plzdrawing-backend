package org.example.plzdrawing.common.error;

import org.springframework.http.HttpStatus;

public interface ErrorCode {

    int getCode();
    HttpStatus getHttpStatus();
    String getMessage();
}
