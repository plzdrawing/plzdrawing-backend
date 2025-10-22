package org.example.plzdrawing.api.member.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.plzdrawing.api.auth.customuser.CustomUser;
import org.example.plzdrawing.api.member.facade.MemberFacade;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.plzdrawing.api.member.dto.request.UpdateProfileRequest;
import org.example.plzdrawing.api.member.dto.response.ProfileResponse;
import org.example.plzdrawing.api.member.service.MemberService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
@Tag(name = "멤버 관련 컨트롤러", description = "MemberController")
public class MemberController {

    private final MemberFacade memberFacade;

    @PostMapping(value = "/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Boolean> uploadFile(
            @AuthenticationPrincipal CustomUser customUser,
            @RequestPart("multipartFile") MultipartFile multipartFile,
            @RequestPart("introduce") String introduce,
            @RequestPart("hashTag") List<String> hashTag){
        return ResponseEntity.ok((memberFacade.uploadProfile(customUser, multipartFile, introduce, hashTag)));
    }

    private final MemberService memberService;

    @PatchMapping("/v1/profile")
    @Operation(summary = "프로필 수정", description = "닉네임, 한 줄 소개, 해시태그, 프로필 이미지 수정")
    public ResponseEntity<ProfileResponse> updateProfile(
            @AuthenticationPrincipal CustomUser customUser,
            @RequestBody UpdateProfileRequest request
    ) {
        Long memberId = customUser.getMember().getId();
        ProfileResponse response = memberService.updateProfile(memberId, request);
        return ResponseEntity.ok(response);
    }
}
