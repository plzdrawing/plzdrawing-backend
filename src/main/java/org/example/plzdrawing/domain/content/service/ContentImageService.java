package org.example.plzdrawing.domain.content.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.example.plzdrawing.domain.content.Content;
import org.example.plzdrawing.domain.content.ContentImage;
import org.example.plzdrawing.domain.content.repository.ContentImageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ContentImageService {
    private final ContentImageRepository contentImageRepository;

    @Transactional
    public void uploadImages(Content content, List<String> fileNames) {
        List<ContentImage> images = fileNames.stream()
                .map(fileName -> ContentImage.builder()
                        .url(fileName)
                        .content(content)
                        .build())
                .collect(Collectors.toList());
        contentImageRepository.saveAll(images);
    }
}
