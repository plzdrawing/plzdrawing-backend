package org.example.plzdrawing.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LikeEntityRepository extends JpaRepository<LikeEntity, Long> {
    interface LikeCountProjection {
        Long getContentId();
        Long getLikeCount();
    }

    @Query("""
        select l.content.id as contentId, count(l) as likeCount
        from LikeEntity l
        where l.content.id in :contentIds
        group by l.content.id
        """)
    List<LikeCountProjection> countLikesByContentIds(@Param("contentIds") List<Long> contentIds);
}
