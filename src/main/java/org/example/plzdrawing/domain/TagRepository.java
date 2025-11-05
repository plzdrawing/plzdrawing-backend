package org.example.plzdrawing.domain;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TagRepository extends JpaRepository<Tag, Long> {
    @Query("select t from Tag t where lower(t.content) in :contents and t.status = 'ACTIVE'")
    List<Tag> findActiveByContentInIgnoreCase(@Param("contents") Collection<String> contents);

    @Query("select t from Tag t where lower(t.content) = lower(:content) and t.status = 'ACTIVE'")
    Optional<Tag> findActiveByContentIgnoreCase(@Param("content") String content);

    @Query("""
SELECT t FROM Tag t
WHERE LOWER(t.content) IN :norms
AND t.status = :status
""")
    List<Tag> findActiveByContentInIgnoreCase(@Param("norms") Collection<String> norms,
            @Param("status") Status status);
}
