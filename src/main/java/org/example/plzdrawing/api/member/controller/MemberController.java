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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
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
}
