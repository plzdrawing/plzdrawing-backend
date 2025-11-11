package org.example.plzdrawing.domain.card.service;

import lombok.RequiredArgsConstructor;
import org.example.plzdrawing.domain.card.Card;
import org.example.plzdrawing.domain.card.dto.UploadCardRequest;
import org.example.plzdrawing.domain.card.repository.CardRepository;
import org.example.plzdrawing.domain.member.Member;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CardService {
    private final CardRepository cardRepository;

    @Transactional
    public Card uploadCard(Member member, UploadCardRequest uploadCardRequest, String url) {
        return cardRepository.save(
                Card.builder()
                        .title(uploadCardRequest.title())
                        .url(url)
                        .price(uploadCardRequest.price())
                        .timeTaken(uploadCardRequest.timeTaken())
                        .isModify(uploadCardRequest.isModify())
                        .member(member)
                        .build()
        );
    }
}
