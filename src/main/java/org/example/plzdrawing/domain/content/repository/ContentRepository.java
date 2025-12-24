package org.example.plzdrawing.domain.content.repository;

import java.util.List;
import java.util.Optional;
import org.example.plzdrawing.domain.content.Content;
import org.example.plzdrawing.domain.member.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ContentRepository extends JpaRepository<Content, Long> {
    Optional<Content> findByMemberAndId(Member member, Long contentId);

    Page<Content> findByMemberId(Long memberId, Pageable pageable);

    Page<Content> findAllByOrderByCreatedAtDesc(Pageable pageable);

    interface DrawingCountProjection {
        Long getMemberId();
        Long getCnt();
    }

    @Query("""
        select c.member.id as memberId, count(c) as cnt
        from Content c
        where c.member.id in :memberIds
        group by c.member.id
    """)
    List<DrawingCountProjection> countByMemberIds(@Param("memberIds") List<Long> memberIds);
}
