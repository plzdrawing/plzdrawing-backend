package org.example.plzdrawing.domain.content.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.plzdrawing.api.auth.customuser.CustomUser;
import org.example.plzdrawing.domain.content.dto.response.ContentsDto;
import org.example.plzdrawing.common.page.PageResponse;
import org.example.plzdrawing.domain.content.dto.request.UpdateContentRequest;
import org.example.plzdrawing.domain.content.dto.request.UploadContentRequest;
import org.example.plzdrawing.domain.content.dto.response.LatestContentsResponse;
import org.example.plzdrawing.domain.content.dto.response.UploadContentResponse;
import org.example.plzdrawing.domain.content.facade.ContentFacade;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @GetMapping("/{memberId}")
    @Operation(summary = "멤버별 콘텐츠 조회", description = "멤버별 콘텐츠를 페이징으로 조회합니다.")
    public ResponseEntity<PageResponse<ContentsDto>> getContentsThumbnail(
            @PathVariable Long memberId,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        int safePage = (page < 1) ? 0 : page - 1;
        return ResponseEntity.ok(contentFacade.getMemberContents(memberId, safePage, size));
    }

    @GetMapping
    @Operation(summary = "최신순 콘텐츠 조회", description = "최신순으로 콘텐츠를 조회한다.")
    public ResponseEntity<PageResponse<LatestContentsResponse>> getLatestContents(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        int safePage = (page < 1) ? 0 : page - 1;
        return ResponseEntity.ok(contentFacade.getLatestContents(safePage, size));
    }
}
