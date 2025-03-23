package org.example.plzdrawing.util.s3.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.plzdrawing.common.error.BaseErrorCode;
import org.example.plzdrawing.common.error.ErrorCode;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum S3ErrorCode { //3
    UNPROCESSABLE_IMAGE(new BaseErrorCode(4223, HttpStatus.UNPROCESSABLE_ENTITY, "업로드된 이미지를 처리할 수 없습니다.")),
    UPLOAD_FAILED(new BaseErrorCode(5001, HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드에 실패했습니다.")),
    DELETE_FAILED(new BaseErrorCode(5002, HttpStatus.INTERNAL_SERVER_ERROR, "파일 삭제에 실패했습니다.")),
    GET_URL_FAILED(new BaseErrorCode(5003, HttpStatus.INTERNAL_SERVER_ERROR, "파일 URL 발급에 실패했습니다."));

    private final ErrorCode errorCode;
}