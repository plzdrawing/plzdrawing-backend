package org.example.plzdrawing.domain.content.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.plzdrawing.api.auth.customuser.CustomUser;
import org.example.plzdrawing.domain.content.dto.request.UpdateContentRequest;
import org.example.plzdrawing.domain.content.dto.request.UploadContentRequest;
import org.example.plzdrawing.domain.content.dto.response.UploadContentResponse;
import org.example.plzdrawing.domain.content.facade.ContentFacade;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/content")
@Tag(name = "컨텐츠 관련 컨트롤러", description = "ContentController")
public class ContentController {
    private final ContentFacade contentFacade;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "게시글 업로드", description = "uploadContents")
    public ResponseEntity<UploadContentResponse> uploadContents(
            @AuthenticationPrincipal CustomUser customUser,
            @RequestPart("multipartFile") List<MultipartFile> multipartFile,
            @RequestPart("content") @Valid UploadContentRequest uploadContentRequest
    ) {
        return ResponseEntity.ok(contentFacade.uploadContents(customUser, multipartFile, uploadContentRequest));
    }

    @PatchMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "게시글 수정", description = "updateContents")
    public ResponseEntity<Boolean> updateContents(
            @AuthenticationPrincipal CustomUser customUser,
            @RequestPart("multipartFile") List<MultipartFile> multipartFile,
            @RequestPart("content") @Valid UpdateContentRequest updateContentRequest
    ) {
        return ResponseEntity.ok(contentFacade.updateContents(customUser, multipartFile, updateContentRequest));
    }
}
