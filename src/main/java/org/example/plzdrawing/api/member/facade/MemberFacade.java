package org.example.plzdrawing.api.member.facade;

import lombok.RequiredArgsConstructor;
import org.example.plzdrawing.api.auth.customuser.CustomUser;
import org.example.plzdrawing.api.member.dto.request.UpdateProfileRequest;
import org.example.plzdrawing.api.member.dto.request.UpsertProfileRequest;
import org.example.plzdrawing.api.member.dto.response.ProfileResponse;
import org.example.plzdrawing.api.member.service.MemberService;
import org.example.plzdrawing.api.member.service.ProfileTagService;
import org.example.plzdrawing.domain.member.Member;
import org.example.plzdrawing.util.s3.S3Service;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
public class MemberFacade {
    private final MemberService memberService;
    private final S3Service s3Service;
    private final ProfileTagService profileTagService;
    public Boolean uploadProfile(CustomUser customUser, MultipartFile multipartFile, UpsertProfileRequest upsertProfileRequest) {
        //프로필 업로드
        Member member = customUser.getMember();
        String fileName = s3Service.uploadFile(member.getId(), multipartFile);
        memberService.upsertProfile(member, fileName, upsertProfileRequest.introduce());
        profileTagService.syncMemberTags(member.getId(), upsertProfileRequest.hashTag());
        return true;
    }

    @Transactional
    public ProfileResponse updateProfile(Long memberId, MultipartFile multipartFile, UpdateProfileRequest updateProfileRequest) {
        Member member = memberService.findById(memberId);
        String fileName = s3Service.uploadFile(member.getId(), multipartFile);
        member.updateNickName(updateProfileRequest.nickname());
        memberService.upsertProfile(member, fileName, updateProfileRequest.introduce());
        profileTagService.syncMemberTags(member.getId(), updateProfileRequest.hashTag());
        return new ProfileResponse(
                updateProfileRequest.nickname(),
                updateProfileRequest.introduce(),
                updateProfileRequest.hashTag(),
                fileName
        );
    }
}
