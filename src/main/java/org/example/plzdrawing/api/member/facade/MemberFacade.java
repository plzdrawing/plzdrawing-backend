package org.example.plzdrawing.api.member.facade;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.plzdrawing.api.auth.customuser.CustomUser;
import org.example.plzdrawing.api.member.service.MemberService;
import org.example.plzdrawing.api.member.service.ProfileTagService;
import org.example.plzdrawing.domain.member.Member;
import org.example.plzdrawing.util.s3.S3Service;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
public class MemberFacade {
    private final MemberService memberService;
    private final S3Service s3Service;
    private final ProfileTagService profileTagService;
    public Boolean uploadProfile(CustomUser customUser, MultipartFile multipartFile, String introduce, List<String> hashTag) {
        //프로필 업로드
        Member member = customUser.getMember();
        String fileName = s3Service.uploadFile(member.getId(), multipartFile);
        memberService.makeProfile(member, fileName, introduce);
        profileTagService.syncMemberTags(member.getId(), hashTag);
        return true;
    }
}
