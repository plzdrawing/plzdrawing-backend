package org.example.plzdrawing.domain.content.service;

import java.util.ArrayList;
import java.util.Collections;
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

        // 2) 태그 로드/생성(upsert) → 최종 활성 Tag 맵(by lower-content)
        Map<String, Tag> tagsByNorm = loadOrCreateTags(normalized, normToOriginal);

        // 3) 이번 요청에서 필요한 태그 ID 집합
        Set<Long> requiredTagIds = tagsByNorm.values().stream()
                .map(Tag::getId)
                .collect(Collectors.toCollection(LinkedHashSet::new));

        // 4) 해당 콘텐츠의 기존 링크(상태 무관) 조회
        Map<Long, ContentTag> linkByTagId = fetchExistingLinksMap(contentId, requiredTagIds);

        // 5) 집합 연산으로 대상 계산
        Set<Long> existingTagIds = linkByTagId.keySet();

        // 새로 만들어야 할 링크 = required - existing
        Set<Long> toCreateIds = new LinkedHashSet<>(requiredTagIds);
        toCreateIds.removeAll(existingTagIds);

        // 활성화할 링크 = required ∩ existing 중 INACTIVE
        List<ContentTag> toActivate = linkByTagId.entrySet().stream()
                .filter(e -> requiredTagIds.contains(e.getKey()))
                .map(Map.Entry::getValue)
                .filter(ct -> ct.getStatus() != Status.ACTIVE)
                .toList();

        // 비활성화할 링크 = 현재 ACTIVE 중 required에 없는 것
        List<ContentTag> currentActive = contentTagRepository.findByContentIdAndStatus(contentId, Status.ACTIVE);
        List<ContentTag> toDeactivate = currentActive.stream()
                .filter(ct -> !requiredTagIds.contains(ct.getTag().getId()))
                .toList();

        // 6) 반영
        applyLinkChanges(content, tagsByNorm, toCreateIds, toActivate, toDeactivate);
    }

    private Map<Long, ContentTag> fetchExistingLinksMap(Long contentId, Set<Long> requiredTagIds) {
        if (requiredTagIds.isEmpty()) return Collections.emptyMap();
        List<ContentTag> existingLinks = contentTagRepository.findByContentIdAndTagIdIn(contentId, new ArrayList<>(requiredTagIds));
        return existingLinks.stream().collect(Collectors.toMap(ct -> ct.getTag().getId(), ct -> ct));
    }

    /**
     * 태그를 (이름-대소문자 무시) 기준으로 로드하고 없으면 생성.
     * 유니크 제약으로 인한 동시성 충돌을 1회 재조회로 보정.
     */
    private Map<String, Tag> loadOrCreateTags(Set<String> normalized, Map<String, String> normToOriginal) {
        // 1차 로드
        List<Tag> existing = tagRepository.findActiveByContentInIgnoreCase(normalized, Status.ACTIVE);
        Map<String, Tag> byNorm = existing.stream()
                .collect(Collectors.toMap(t -> t.getContent().toLowerCase(), t -> t));

        // 누락분 생성
        Map<String, Tag> finalByNorm = byNorm;
        List<Tag> toCreate = normalized.stream()
                .filter(n -> !finalByNorm.containsKey(n))
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
                // 다른 트랜잭션이 먼저 만든 경우 → 아래에서 재조회로 수습
            }
            // 재조회로 최종 정합 보장
            List<Tag> reloaded = tagRepository.findActiveByContentInIgnoreCase(normalized, Status.ACTIVE);
            byNorm = reloaded.stream()
                    .collect(Collectors.toMap(t -> t.getContent().toLowerCase(), t -> t));
        }
        return byNorm;
    }

    private void applyLinkChanges(
            Content content,
            Map<String, Tag> tagsByNorm,
            Set<Long> toCreateIds,
            List<ContentTag> toActivate,
            List<ContentTag> toDeactivate
    ) {
        // 생성
        if (!toCreateIds.isEmpty()) {
            // tagId → Tag 역조회용 맵
            Map<Long, Tag> tagById = tagsByNorm.values().stream()
                    .collect(Collectors.toMap(Tag::getId, t -> t));

            List<ContentTag> toCreateLinks = toCreateIds.stream()
                    .map(id -> new ContentTag(content, tagById.get(id)))
                    .toList();

            // FK 유니크(uk(content_id, tag_id))로 동시성 충돌 가능 → 예외시 트랜잭션 롤백 or catch 후 무시 전략
            contentTagRepository.saveAll(toCreateLinks);
        }

        // 활성화
        toActivate.forEach(ContentTag::activate);

        // 비활성화
        toDeactivate.forEach(ContentTag::deactivate);
    }
}
