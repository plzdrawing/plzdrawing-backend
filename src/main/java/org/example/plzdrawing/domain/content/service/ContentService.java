package org.example.plzdrawing.domain.content.service;

import lombok.RequiredArgsConstructor;
import org.example.plzdrawing.domain.content.Content;
import org.example.plzdrawing.domain.content.dto.UploadContentRequest;
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
                        .explanation(uploadContentRequest.explain())
                        .price(uploadContentRequest.price())
                        .timeTaken(uploadContentRequest.timeTaken())
                        .build()
        );
    }
}
