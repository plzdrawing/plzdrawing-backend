package org.example.plzdrawing.domain.content.service;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.example.plzdrawing.domain.content.Content;
import org.example.plzdrawing.domain.content.dto.request.UpdateContentRequest;
import org.example.plzdrawing.domain.content.dto.request.UploadContentRequest;
import org.example.plzdrawing.domain.content.repository.ContentRepository;
import org.example.plzdrawing.domain.member.Member;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ContentService {
    private final ContentRepository contentRepository;

    @Transactional
    public Content upload(Member member, UploadContentRequest uploadContentRequest) {
        return contentRepository.save(
                Content.builder()
                        .member(member)
                        .title(uploadContentRequest.title())
                        .explanation(uploadContentRequest.explain())
                        .price(uploadContentRequest.price())
                        .timeTaken(uploadContentRequest.timeTaken())
                        .build()
        );
    }

    @Transactional
    public void update(Content content, UpdateContentRequest updateContentRequest) {
        content.update(updateContentRequest);
    }

    @Transactional(readOnly = true)
    public Optional<Content> findByMemberAndId(Member member, Long contentId) {
        return contentRepository.findByMemberAndId(member, contentId);
    }
}
