package org.example.plzdrawing.domain.content.facade;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.plzdrawing.api.auth.customuser.CustomUser;
import org.example.plzdrawing.domain.content.Content;
import org.example.plzdrawing.domain.content.dto.UploadContentResponse;
import org.example.plzdrawing.domain.content.service.ContentImageService;
import org.example.plzdrawing.domain.content.service.ContentService;
import org.example.plzdrawing.domain.content.service.ContentTagService;
import org.example.plzdrawing.domain.member.Member;
import org.example.plzdrawing.util.s3.S3Service;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
public class ContentFacade {
    private final S3Service s3Service;
    private final ContentService contentService;
    private final ContentImageService contentImageService;
    private final ContentTagService contentTagService;

    public UploadContentResponse uploadContents(CustomUser customUser, List<MultipartFile> multipartFile, String explain, List<String> hashTag) {
        Member member = customUser.getMember();
        List<String> fileNames = s3Service.uploadFile(member.getId(), multipartFile, 5);
        Content content = contentService.upload(member, explain);
        contentImageService.uploadImages(content, fileNames);
        contentTagService.syncContentTags(content.getId(), hashTag);
        return UploadContentResponse.of(content.getId());
    }
}
