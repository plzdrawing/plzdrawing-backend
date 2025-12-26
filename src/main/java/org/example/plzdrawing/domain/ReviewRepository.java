package org.example.plzdrawing.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    interface ReviewStatsProjection {
        Long getSellingMemberId();
        Long getReviewCount();
        Float getAvgRating();
    }

    @Query("""
        select r.sellingMember.id as sellingMemberId,
               count(r) as reviewCount,
               avg(r.totalRating) as avgRating
        from Review r
        where r.sellingMember.id in :memberIds
        group by r.sellingMember.id
    """)
    List<ReviewStatsProjection> findStatsBySellingMemberIds(@Param("memberIds") List<Long> memberIds);
}
