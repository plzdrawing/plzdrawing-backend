package org.example.plzdrawing.api.member.controller;

import jakarta.validation.Valid;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.plzdrawing.api.auth.customuser.CustomUser;
import org.example.plzdrawing.api.member.dto.request.UpsertProfileRequest;
import org.example.plzdrawing.api.member.dto.response.ProfileInfoResponse;
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
    private final MemberService memberService;


    @PostMapping(value = "/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "프로필 업로드", description = "프로필 업로드")
    public ResponseEntity<Boolean> uploadFile(
            @AuthenticationPrincipal CustomUser customUser,
            @RequestPart("multipartFile") MultipartFile multipartFile,
            @RequestPart("profile") @Valid UpsertProfileRequest upsertProfileRequest) {
        return ResponseEntity.ok((memberFacade.uploadProfile(customUser, multipartFile, upsertProfileRequest)));
    }
    
    @PatchMapping("/v1/profile")
    @Operation(summary = "프로필 수정", description = "닉네임, 한 줄 소개, 해시태그, 프로필 이미지 수정")
    public ResponseEntity<ProfileResponse> updateProfile(
            @AuthenticationPrincipal CustomUser customUser,
            @RequestPart("multipartFile") MultipartFile multipartFile,
            @RequestPart("profile") UpdateProfileRequest request
    ) {
        Long memberId = customUser.getMember().getId();
        ProfileResponse response = memberFacade.updateProfile(memberId, multipartFile, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/v1/me")
    @Operation(summary = "마이페이지 사용자 정보 조회", description = "JWT 토큰 기반으로 현재 로그인된 사용자의 정보를 조회합니다.")
    public ResponseEntity<ProfileInfoResponse> getMyProfile(
            @AuthenticationPrincipal CustomUser customUser
    ) {
        Long memberId = customUser.getMember().getId();
        ProfileInfoResponse response = memberService.getMyProfile(memberId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/v1/withdraw")
    @Operation(summary = "회원 탈퇴", description = "현재 로그인된 회원을 탈퇴시킵니다.")
    public ResponseEntity<Void> withdraw(
            @AuthenticationPrincipal CustomUser customUser,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        memberService.withdraw(customUser.getMember().getId(), request, response);
        return ResponseEntity.noContent().build();
    }
}
