package org.example.plzdrawing.domain.content.repository;

import java.util.Optional;
import org.example.plzdrawing.api.member.dto.response.ContentThumbnailResponse;
import org.example.plzdrawing.domain.content.Content;
import org.example.plzdrawing.domain.member.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ContentRepository extends JpaRepository<Content, Long> {
    Optional<Content> findByMemberAndId(Member member, Long contentId);

    @Query("""
        SELECT new org.example.plzdrawing.api.member.dto.response.ContentThumbnailResponse(
            c.id,
            (
                SELECT ci.url
                FROM ContentImage ci
                WHERE ci.content = c
                ORDER BY ci.id ASC
                LIMIT 1
            )
        )
        FROM Content c
        WHERE c.member.id = :memberId
        """)
    Page<ContentThumbnailResponse> findThumbnailsByMemberId(
            @Param("memberId") Long memberId,
            Pageable pageable
    );
}
