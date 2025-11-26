package org.example.plzdrawing.domain.content.repository;

import java.util.List;
import org.example.plzdrawing.domain.content.Content;
import org.example.plzdrawing.domain.content.ContentImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContentImageRepository extends JpaRepository<ContentImage, Long> {
    List<ContentImage> findAllByContent(Content content);
    void deleteAllByContent(Content content);
}
