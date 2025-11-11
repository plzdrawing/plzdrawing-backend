package org.example.plzdrawing.domain.card.repository;

import org.example.plzdrawing.domain.card.Card;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<Card, Long> {

}
