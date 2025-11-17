package org.example.plzdrawing.alarm.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.example.plzdrawing.alarm.service.FCMService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/fcm")
@Tag(name = "fcm 관련 테스트 컨트롤러", description = "TestController")
public class FcmTestController {
    private final FCMService fcmService;
    @PostMapping("/v1/test")
    @Operation(summary = "fcm 테스트", description = "fcm")
    public ResponseEntity<Boolean> fcmTest() throws IOException {
        fcmService.sendMessageTo("target", "알림 테스트", "알림 내용", "알림 링크");
        return ResponseEntity.ok(true);
    }
}
