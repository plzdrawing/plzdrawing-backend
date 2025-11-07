package org.example.plzdrawing.domain.content.repository;

import org.example.plzdrawing.domain.content.Content;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContentRepository extends JpaRepository<Content, Long> {

}
