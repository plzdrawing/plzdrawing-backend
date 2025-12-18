package org.example.plzdrawing.domain.content.repository;

import java.util.Optional;
import org.example.plzdrawing.domain.content.Content;
import org.example.plzdrawing.domain.member.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContentRepository extends JpaRepository<Content, Long> {
    Optional<Content> findByMemberAndId(Member member, Long contentId);

    Page<Content> findByMemberId(Long memberId, Pageable pageable);
}
