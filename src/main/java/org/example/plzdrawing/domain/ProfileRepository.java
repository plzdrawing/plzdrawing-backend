package org.example.plzdrawing.domain;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
    Optional<Profile> findByMemberId(Long memberId);

    interface ProfileProjection {
        Long getMemberId();
        String getProfileUrl();
    }

    @Query("""
        select p.member.id as memberId, p.profileUrl as profileUrl
        from Profile p
        where p.member.id in :memberIds
    """)
    List<ProfileProjection> findProfilesByMemberIds(@Param("memberIds") List<Long> memberIds);
}
