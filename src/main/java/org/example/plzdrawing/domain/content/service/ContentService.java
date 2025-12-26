package org.example.plzdrawing.domain.content.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.example.plzdrawing.domain.content.Content;
import org.example.plzdrawing.domain.content.dto.request.UpdateContentRequest;
import org.example.plzdrawing.domain.content.dto.request.UploadContentRequest;
import org.example.plzdrawing.domain.content.repository.ContentRepository;
import org.example.plzdrawing.domain.member.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Transactional(readOnly = true)
    public Page<Content> findContents(Long memberId, Pageable pageable) {
        return (memberId == null)
                ? contentRepository.findAll(pageable)
                : contentRepository.findByMemberId(memberId, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Content> findLatest(Pageable pageable) {
        return contentRepository.findAllByOrderByCreatedAtDesc(pageable);
    }

    @Transactional(readOnly = true)
    public Map<Long, Long> countDrawingsByMemberIds(List<Long> memberIds) {
        if (memberIds.isEmpty()) return Map.of();

        return contentRepository.countByMemberIds(memberIds).stream()
                .collect(Collectors.toMap(
                        ContentRepository.DrawingCountProjection::getMemberId,
                        ContentRepository.DrawingCountProjection::getCnt
                ));
    }
}
