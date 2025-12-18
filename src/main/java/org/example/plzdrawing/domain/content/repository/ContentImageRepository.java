package org.example.plzdrawing.domain.content.repository;

import java.util.List;
import org.example.plzdrawing.domain.content.Content;
import org.example.plzdrawing.domain.content.ContentImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ContentImageRepository extends JpaRepository<ContentImage, Long> {
    List<ContentImage> findAllByContent(Content content);
    void deleteAllByContent(Content content);

    interface ContentImageProjection {
        Long getContentId();
        String getUrl();
    }

    @Query("""
        select ci.content.id as contentId, ci.url as url
        from ContentImage ci
        where ci.content.id in :contentIds
        """)
    List<ContentImageProjection> findAllByContentIds(@Param("contentIds") List<Long> contentIds);
}
