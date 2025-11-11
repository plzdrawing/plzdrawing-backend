package org.example.plzdrawing.domain.card.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.example.plzdrawing.domain.card.enums.TimeTaken;

public record UploadCardRequest(
        @Schema(description = "제목", example = "카드 제목")
        String title,
        @Schema(description = "예상금액", example = "1000")
        Long price,
        @Schema(description = "예상소요시간", example = "TEN / HALF_HOUR / MORE_THAN_AN_HOUR / DAY")
        TimeTaken timeTaken,
        @Schema(description = "그림 수정 여부", example = "true")
        Boolean isModify
) {

}
