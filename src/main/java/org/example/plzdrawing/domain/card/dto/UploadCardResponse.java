package org.example.plzdrawing.domain.card.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record UploadCardResponse(
        @Schema(description = "cardId", example = "1")
        Long cardId
) {
    public static UploadCardResponse of(
            Long cardId
    ) {
        return new UploadCardResponse(
                cardId
        );
    }
}
