package org.example.plzdrawing.api.member.facade;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.plzdrawing.api.auth.customuser.CustomUser;
import org.example.plzdrawing.api.member.dto.request.UpsertProfileRequest;
import org.example.plzdrawing.api.member.dto.response.ContentThumbnailResponse;
import org.example.plzdrawing.api.member.service.MemberService;
import org.example.plzdrawing.api.member.service.ProfileTagService;
import org.example.plzdrawing.common.page.PageResponse;
import org.example.plzdrawing.domain.content.service.ContentService;
import org.example.plzdrawing.domain.member.Member;
import org.example.plzdrawing.util.s3.S3Service;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
public class MemberFacade {
    private final MemberService memberService;
    private final S3Service s3Service;
    private final ProfileTagService profileTagService;
    private final ContentService contentService;
    public Boolean uploadProfile(CustomUser customUser, MultipartFile multipartFile, UpsertProfileRequest upsertProfileRequest) {
        //프로필 업로드
        Member member = customUser.getMember();
        String fileName = s3Service.uploadFile(member.getId(), multipartFile);
        memberService.makeProfile(member, fileName, upsertProfileRequest.introduce());
        profileTagService.syncMemberTags(member.getId(), upsertProfileRequest.hashTag());
        return true;
    }

    public PageResponse<ContentThumbnailResponse> getContentsThumbnail(Long memberId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        PageResponse<ContentThumbnailResponse> pageResponse =
                contentService.getMemberContentThumbnails(memberId, pageRequest);

        List<ContentThumbnailResponse> converted = pageResponse.data().stream()
                .map(item -> new ContentThumbnailResponse(
                        item.contentId(),
                        convertToUrl(item.thumbnailUrl())
                ))
                .toList();

        // PageResponse로 변환
        return new PageResponse<>(
                converted,
                pageResponse.totalPages(),
                pageResponse.isLastPage()
        );
    }

    private String convertToUrl(String key) {
        return (key == null || key.isBlank())
                ? null
                : s3Service.getFileUrl(key);
    }
}
