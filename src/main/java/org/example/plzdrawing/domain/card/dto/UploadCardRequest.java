package org.example.plzdrawing.domain.card.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record UploadCardRequest(
        @Schema(description = "제목", example = "카드 제목")
        String title,
        @Schema(description = "예상금액", example = "1000")
        Long price,
        @Schema(description = "예상소요시간", example = "10분~20분")
        String timeTaken,
        @Schema(description = "그림 수정 여부", example = "true")
        Boolean isModify
) {

}
