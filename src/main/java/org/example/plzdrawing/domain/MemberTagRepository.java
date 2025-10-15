package org.example.plzdrawing.domain;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.example.plzdrawing.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberTagRepository extends JpaRepository<MemberTag, Long> {

    List<MemberTag> findByMemberIdAndStatus(Long memberId, Status status);

    @Query("select mt from MemberTag mt " +
            "where mt.member.id = :memberId and lower(mt.tag.content) in :contents")
    List<MemberTag> findByMemberAndTagContentsIgnoreCase(@Param("memberId") Long memberId,
            @Param("contents") Collection<String> contents);

    Optional<MemberTag> findByMemberAndTag(Member member, Tag tag);
}
