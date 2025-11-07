package org.example.plzdrawing.api.member.service;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.example.plzdrawing.domain.MemberTag;
import org.example.plzdrawing.domain.MemberTagRepository;
import org.example.plzdrawing.domain.Status;
import org.example.plzdrawing.domain.Tag;
import org.example.plzdrawing.domain.TagRepository;
import org.example.plzdrawing.domain.member.Member;
import org.example.plzdrawing.domain.member.MemberRepository;
import org.example.plzdrawing.util.tag.TagUtil;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProfileTagServiceImpl implements ProfileTagService {

    private final TagRepository tagRepository;
    private final MemberTagRepository memberTagRepository;
    private final MemberRepository memberRepository;

    @Override
    public void syncMemberTags(Long memberId, List<String> hashTags) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
        // 1) 입력 정규화 (trim, 빈 값 제거, 중복 제거, 소문자 키)
        Map<String, String> normalizedToOriginal = TagUtil.normalize(hashTags);
        Set<String> normalized = normalizedToOriginal.keySet(); // lower-case set

        // 2) 이미 존재하는 활성 Tag 조회
        List<Tag> existingTags = tagRepository.findActiveByContentInIgnoreCase(normalized);
        Map<String, Tag> byNorm = existingTags.stream()
                .collect(Collectors.toMap(t -> t.getContent().toLowerCase(), t -> t));

        // 3) 없는 Tag 생성
        List<Tag> toCreate = normalized.stream()
                .filter(n -> !byNorm.containsKey(n))
                .map(n -> new Tag(
                        null,
                        normalizedToOriginal.get(n), // 원본 표기 유지
                        Status.ACTIVE,
                        new ArrayList<>(),
                        new LinkedHashSet<>()
                ))
                .collect(Collectors.toList());

        if (!toCreate.isEmpty()) {
            try {
                tagRepository.saveAll(toCreate);
            } catch (DataIntegrityViolationException e) {
                // 동시성(중복 생성) 대비: 다시 읽어서 보정
                List<Tag> rebound = tagRepository.findActiveByContentInIgnoreCase(normalized);
                byNorm.clear();
                rebound.forEach(t -> byNorm.put(t.getContent().toLowerCase(), t));
            }
            toCreate.forEach(t -> byNorm.put(t.getContent().toLowerCase(), t));
        }

        // 4) 현재 회원의 ACTIVE MemberTag들
        List<MemberTag> currentActive = memberTagRepository.findByMemberIdAndStatus(memberId, Status.ACTIVE);
        Map<Long, MemberTag> currentByTagId = currentActive.stream()
                .collect(Collectors.toMap(mt -> mt.getTag().getId(), mt -> mt));

        // 5) 원하는(요청) 태그 집합 → MemberTag 없으면 생성/활성화
        for (String norm : normalized) {
            Tag tag = byNorm.get(norm);
            MemberTag mt = memberTagRepository.findByMemberAndTag(member, tag).orElse(null);
            if (mt == null) {
                // 생성 시 생성자에서 status=ACTIVE 설정
                mt = new MemberTag(member, tag);
                memberTagRepository.save(mt);
            } else if (mt.getStatus() != Status.ACTIVE) {
                mt.activate();
            }
            currentByTagId.remove(tag.getId()); // 유지되는 것들은 제거
        }

        // 6) 요청에 없었던 기존 ACTIVE 태그는 INACTIVE로
        for (MemberTag leftover : currentByTagId.values()) {
            leftover.deactivate();
        }
    }
}
