package org.example.plzdrawing.common.listener.websocket.dto;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class JoinEvent {
    private String chatRoomId;
    private Long memberId;
}
