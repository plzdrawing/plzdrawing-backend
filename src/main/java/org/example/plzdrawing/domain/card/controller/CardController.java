package org.example.plzdrawing.domain.card.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.plzdrawing.api.auth.customuser.CustomUser;
import org.example.plzdrawing.domain.card.dto.UploadCardRequest;
import org.example.plzdrawing.domain.card.dto.UploadCardResponse;
import org.example.plzdrawing.domain.card.facade.CardFacade;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/card")
@Tag(name = "카드 관련 컨트롤러", description = "CardController")
public class CardController {
    private final CardFacade cardFacade;
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "그림카드 업로드", description = "cardContents")
    public ResponseEntity<UploadCardResponse> uploadContents(
            @AuthenticationPrincipal CustomUser customUser,
            @RequestPart("multipartFile") MultipartFile multipartFile,
            @RequestPart("card") UploadCardRequest uploadCardRequest
    ) {
        return ResponseEntity.ok(cardFacade.uploadCard(customUser, multipartFile, uploadCardRequest));
    }
}
