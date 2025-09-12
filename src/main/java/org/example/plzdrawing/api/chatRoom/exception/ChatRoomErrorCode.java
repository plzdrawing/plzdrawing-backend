package org.example.plzdrawing.api.chatRoom.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.plzdrawing.common.error.BaseErrorCode;
import org.example.plzdrawing.common.error.ErrorCode;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ChatRoomErrorCode {
    CHATROOM_NOT_FOUND(new BaseErrorCode("CHATROOM_001", HttpStatus.NOT_FOUND,"존재하지 않는 채팅방입니다"))
    ;

    private final ErrorCode errorCode;
}
