package org.example.plzdrawing.api.chatRoom.dto.request;

import jakarta.validation.constraints.Positive;
import lombok.Getter;

@Getter
public class CreateChatRoomRequest {

    @Positive(message = "판매자 id는 필수입니다.")
    private Long sellerId;
}
