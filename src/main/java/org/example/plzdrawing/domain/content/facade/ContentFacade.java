package org.example.plzdrawing.domain.content.facade;

import static org.example.plzdrawing.common.error.CommonErrorCode.CONTENT_NOT_FOUND;

import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.example.plzdrawing.api.auth.customuser.CustomUser;
import org.example.plzdrawing.api.member.dto.response.GetContentsResponse;
import org.example.plzdrawing.common.exception.RestApiException;
import org.example.plzdrawing.common.page.PageResponse;
import org.example.plzdrawing.domain.content.Content;
import org.example.plzdrawing.domain.content.dto.request.UpdateContentRequest;
import org.example.plzdrawing.domain.content.dto.request.UploadContentRequest;
import org.example.plzdrawing.domain.content.dto.response.UploadContentResponse;
import org.example.plzdrawing.domain.content.service.ContentImageService;
import org.example.plzdrawing.domain.content.service.ContentService;
import org.example.plzdrawing.domain.content.service.ContentTagService;
import org.example.plzdrawing.domain.content.service.LikeEntityService;
import org.example.plzdrawing.domain.member.Member;
import org.example.plzdrawing.util.s3.S3Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
public class ContentFacade {
    private final S3Service s3Service;
    private final ContentService contentService;
    private final ContentImageService contentImageService;
    private final ContentTagService contentTagService;
    private final LikeEntityService likeEntityService;

    public UploadContentResponse uploadContents(CustomUser customUser, List<MultipartFile> multipartFile, UploadContentRequest uploadContentRequest) {
        Member member = customUser.getMember();
        List<String> fileNames = s3Service.uploadFile(member.getId(), multipartFile, 5);
        Content content = contentService.upload(member, uploadContentRequest);
        contentImageService.uploadImages(content, fileNames);
        contentTagService.syncContentTags(content.getId(), uploadContentRequest.hashTag());
        return UploadContentResponse.of(content.getId());
    }

    public Boolean updateContents(CustomUser customUser, List<MultipartFile> multipartFile, UpdateContentRequest updateContentRequest) {
        Member member = customUser.getMember();
        Content content = contentService.findByMemberAndId(member, updateContentRequest.contentId())
                .orElseThrow(() -> new RestApiException(CONTENT_NOT_FOUND.getErrorCode()));
        contentService.update(content, updateContentRequest);
        List<String> oldImages = contentImageService.getImages(content);
        s3Service.deleteFiles(oldImages);
        List<String> newImages = s3Service.uploadFile(member.getId(), multipartFile, 5);
        contentImageService.updateImages(content, newImages);
        contentTagService.deleteAllByContent(content);
        contentTagService.syncContentTags(content.getId(), updateContentRequest.hashTag());
        return true;
    }

    public PageResponse<GetContentsResponse> getMemberContents(Long memberId, int page, int size) {

        PageRequest pageRequest = PageRequest.of(page, size);

        Page<Content> contentPage = contentService.findContents(memberId, pageRequest);

        if (contentPage.isEmpty()) {
            // PageResponse로 빈 응답 반환
            return PageResponse.from(Page.empty(pageRequest));
        }

        List<Content> contents = contentPage.getContent();
        List<Long> contentIds = contents.stream().map(Content::getId).toList();

        Map<Long, List<String>> imageMap = contentImageService.findImageUrlsByContentIds(contentIds);
        Map<Long, List<String>> tagMap = contentTagService.findTagsByContentIds(contentIds);
        Map<Long, Long> likeMap = likeEntityService.countLikesByContentIds(contentIds);

        List<GetContentsResponse> dtoList = contents.stream()
                .map(content -> {
                    Long contentId = content.getId();
                    return new GetContentsResponse(
                            contentId,
                            content.getCreatedAt().toLocalDate(),
                            imageMap.getOrDefault(contentId, List.of()),
                            tagMap.getOrDefault(contentId, List.of()),
                            content.getExplanation(),
                            content.getTimeTaken(),
                            content.getPrice(),
                            likeMap.getOrDefault(contentId, 0L)
                    );
                })
                .toList();

        Page<GetContentsResponse> dtoPage =
                new PageImpl<>(dtoList, pageRequest, contentPage.getTotalElements());

        return PageResponse.from(dtoPage);
    }
}
