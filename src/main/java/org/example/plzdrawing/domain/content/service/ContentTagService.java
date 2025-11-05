package org.example.plzdrawing.domain.content.service;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.example.plzdrawing.domain.Status;
import org.example.plzdrawing.domain.Tag;
import org.example.plzdrawing.domain.TagRepository;
import org.example.plzdrawing.domain.content.Content;
import org.example.plzdrawing.domain.content.ContentTag;
import org.example.plzdrawing.domain.content.repository.ContentRepository;
import org.example.plzdrawing.domain.content.repository.ContentTagRepository;
import org.example.plzdrawing.util.tag.TagUtil;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ContentTagService {
    private final ContentRepository contentRepository;
    private final TagRepository tagRepository;
    private final ContentTagRepository contentTagRepository;

    @Transactional
    public void syncContentTags(Long contentId, List<String> hashTags) {
        Content content = contentRepository.findById(contentId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        // 1) 정규화
        Map<String, String> normToOriginal = TagUtil.normalize(hashTags); // key: lower-case
        Set<String> normalized = normToOriginal.keySet();

        if (normalized.isEmpty()) {
            // 요청이 비었으면 기존 ACTIVE 모두 비활성화
            List<ContentTag> currentActive = contentTagRepository.findByContentIdAndStatus(contentId, Status.ACTIVE);
            currentActive.forEach(ContentTag::deactivate);
            return;
        }

        // 2) 태그 로드
        List<Tag> existing = tagRepository.findActiveByContentInIgnoreCase(normalized, Status.ACTIVE);
        Map<String, Tag> byNorm = existing.stream()
                .collect(Collectors.toMap(t -> t.getContent().toLowerCase(), t -> t));


        // 3) 없는 태그 생성
        Map<String, Tag> finalByNorm1 = byNorm;
        List<Tag> toCreate = normalized.stream()
                .filter(n -> !finalByNorm1.containsKey(n))
                .map(n -> new Tag(
                        null,
                        normToOriginal.get(n), // 원본 표기 저장
                        Status.ACTIVE,
                        new ArrayList<>(),
                        new LinkedHashSet<>()
                ))
                .toList();

        if (!toCreate.isEmpty()) {
            try {
                tagRepository.saveAll(toCreate);
            } catch (DataIntegrityViolationException e) {
                // 동시성으로 다른 트랜잭션이 먼저 만든 경우 재조회
            }
            // 최종적으로 모두 다시 읽어 맵 구성 (동시성 포함)
            List<Tag> reloaded = tagRepository.findActiveByContentInIgnoreCase(normalized, Status.ACTIVE);
            byNorm = reloaded.stream()
                    .collect(Collectors.toMap(t -> t.getContent().toLowerCase(), t -> t));
        }

        // 4) 현재 ACTIVE 맵
        List<ContentTag> currentActive = contentTagRepository.findByContentIdAndStatus(contentId, Status.ACTIVE);
        Map<Long, ContentTag> currentByTagId = currentActive.stream()
                .collect(Collectors.toMap(ct -> ct.getTag().getId(), ct -> ct));

        // 5) 이번에 필요로 하는 태그ID 집합
        Map<String, Tag> finalByNorm = byNorm;
        List<Long> requiredTagIds = normalized.stream()
                .map(n -> finalByNorm.get(n).getId())
                .toList();

        // 6) 이미 존재하는 ContentTag(상태 무관) 한 번에 조회
        List<ContentTag> existingLinks = contentTagRepository.findByContentIdAndTagIdIn(contentId, requiredTagIds);
        Map<Long, ContentTag> linkByTagId = existingLinks.stream()
                .collect(Collectors.toMap(ct -> ct.getTag().getId(), ct -> ct));

        // 7) 생성/활성/비활성 집합 계산
        List<ContentTag> toCreateLinks = new ArrayList<>();
        List<ContentTag> toActivate = new ArrayList<>();

        for (String norm : normalized) {
            Tag tag = byNorm.get(norm);
            ContentTag link = linkByTagId.get(tag.getId());
            if (link == null) {
                // 새로 만든다(자식 생성자에서 컬렉션 건드리지 않음)
                ContentTag ct = new ContentTag(content, tag);
                toCreateLinks.add(ct);
            } else if (link.getStatus() != Status.ACTIVE) {
                toActivate.add(link);
            }
            // 유지되는 것 제거 (나중에 deactivate 제외)
            currentByTagId.remove(tag.getId());
        }

        // 8) 대량 반영
        if (!toCreateLinks.isEmpty()) {
            // FK 유니크 충돌 방지: uk(content_id, tag_id) 가 있으므로
            // 혹시 동시성으로 같은 링크가 생기면 여기서 예외 → 한번 재시도/무시 전략
            contentTagRepository.saveAll(toCreateLinks);
        }
        toActivate.forEach(ContentTag::activate);

        // 9) 요청에 없던 기존 ACTIVE 비활성화
        for (ContentTag leftover : currentByTagId.values()) {
            leftover.deactivate();
        }
    }
}
