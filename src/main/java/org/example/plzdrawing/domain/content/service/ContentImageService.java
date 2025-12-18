package org.example.plzdrawing.domain.content.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.example.plzdrawing.domain.content.Content;
import org.example.plzdrawing.domain.content.ContentImage;
import org.example.plzdrawing.domain.content.repository.ContentImageRepository;
import org.example.plzdrawing.util.s3.S3Service;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ContentImageService {
    private final ContentImageRepository contentImageRepository;
    private final S3Service s3Service;

    @Transactional
    public void uploadImages(Content content, List<String> fileNames) {
        List<ContentImage> images = fileNames.stream()
                .map(fileName -> ContentImage.builder()
                        .url(fileName)
                        .content(content)
                        .build())
                .toList();
        contentImageRepository.saveAll(images);
    }

    @Transactional(readOnly = true)
    public List<String> getImages(Content content) {
        return contentImageRepository
                .findAllByContent(content)
                .stream()
                .map(ContentImage::getUrl)
                .toList();
    }

    @Transactional
    public void updateImages(Content content, List<String> newImages) {
        contentImageRepository.deleteAllByContent(content);
        List<ContentImage> images = newImages.stream()
                .map(fileName -> ContentImage.builder()
                        .url(fileName)
                        .content(content)
                        .build())
                .toList();
        contentImageRepository.saveAll(images);
    }

    @Transactional(readOnly = true)
    public Map<Long, List<String>> findImageUrlsByContentIds(List<Long> contentIds) {
        if (contentIds.isEmpty()) return Map.of();

        return contentImageRepository.findAllByContentIds(contentIds).stream()
                .collect(Collectors.groupingBy(
                        ContentImageRepository.ContentImageProjection::getContentId,
                        Collectors.mapping(p -> s3Service.getFileUrl(p.getUrl()), Collectors.toList())
                ));
    }
}
