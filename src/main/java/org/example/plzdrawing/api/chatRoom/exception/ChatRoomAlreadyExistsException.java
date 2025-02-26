package org.example.plzdrawing.api.chatRoom.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.plzdrawing.domain.chat.room.ChatRoom;

@Getter
@RequiredArgsConstructor
public class ChatRoomAlreadyExistsException extends RuntimeException {

    private final ChatRoom chatRoom;
}
