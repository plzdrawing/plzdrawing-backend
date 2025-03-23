package org.example.plzdrawing.api.chatRoom.controller;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.plzdrawing.api.auth.customuser.CustomUser;
import org.example.plzdrawing.api.chatRoom.dto.request.CreateChatRoomRequest;
import org.example.plzdrawing.api.chatRoom.dto.response.ResponseChatRoom;
import org.example.plzdrawing.api.chatRoom.service.ChatRoomService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chatRooms")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @PostMapping("/")
    public ResponseEntity<Void> createChatRoom(@AuthenticationPrincipal CustomUser customUser, @Valid @RequestBody
            CreateChatRoomRequest request) {
        String chatRoomId = chatRoomService.createChatRoom(customUser.getMemberId(), request);

        URI uri = URI.create("/api/v1/chat/" + chatRoomId);
        return ResponseEntity.created(uri).build();
    }

    @GetMapping("")
    public ResponseEntity<List<ResponseChatRoom>> getChatRooms(@AuthenticationPrincipal CustomUser customUser) {
        List<ResponseChatRoom> chatRooms = chatRoomService.getChatRooms(customUser.getMemberId());
        return ResponseEntity.ok(chatRooms);
    }
}
