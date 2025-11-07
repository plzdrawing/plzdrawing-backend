package org.example.plzdrawing.domain.card.facade;

import lombok.RequiredArgsConstructor;
import org.example.plzdrawing.api.auth.customuser.CustomUser;
import org.example.plzdrawing.domain.card.Card;
import org.example.plzdrawing.domain.card.dto.UploadCardRequest;
import org.example.plzdrawing.domain.card.dto.UploadCardResponse;
import org.example.plzdrawing.domain.card.service.CardService;
import org.example.plzdrawing.domain.member.Member;
import org.example.plzdrawing.util.s3.S3Service;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
public class CardFacade {
    private final CardService cardService;
    private final S3Service s3Service;
    public UploadCardResponse uploadCard(CustomUser customUser, MultipartFile multipartFile, UploadCardRequest uploadCardRequest) {
        Member member = customUser.getMember();
        String url = s3Service.uploadFile(member.getId(), multipartFile);
        Card card = cardService.uploadCard(member, uploadCardRequest, url);
        return UploadCardResponse.of(card.getId());
    }
}
