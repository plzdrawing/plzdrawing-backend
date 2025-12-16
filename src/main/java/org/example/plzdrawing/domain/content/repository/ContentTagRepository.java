package org.example.plzdrawing.domain.content.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.example.plzdrawing.domain.Status;
import org.example.plzdrawing.domain.Tag;
import org.example.plzdrawing.domain.content.Content;
import org.example.plzdrawing.domain.content.ContentTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ContentTagRepository extends JpaRepository<ContentTag, Long> {

    List<ContentTag> findByContentIdAndStatus(Long contentId, Status status);

    Optional<ContentTag> findByContentAndTag(Content content, Tag tag);

    // ContentTag: 해당 콘텐츠 + 주어진 태그ID들 전부(상태 무관) 조회
    @Query("""
SELECT ct FROM ContentTag ct
WHERE ct.content.id = :contentId
AND ct.tag.id IN :tagIds
""")
    List<ContentTag> findByContentIdAndTagIdIn(@Param("contentId") Long contentId,
            @Param("tagIds") Collection<Long> tagIds);

    void deleteAllByContent(Content content);

    interface ContentTagProjection {
        Long getContentId();
        String getTagContent();
    }

    @Query("""
        select ct.content.id as contentId, t.content as tagContent
        from ContentTag ct
        join ct.tag t
        where ct.content.id in :contentIds
          and ct.status = org.example.plzdrawing.domain.Status.ACTIVE
          and t.status = org.example.plzdrawing.domain.Status.ACTIVE
""")
    List<ContentTagProjection> findTagsByContentIds(@Param("contentIds") List<Long> contentIds);
}
