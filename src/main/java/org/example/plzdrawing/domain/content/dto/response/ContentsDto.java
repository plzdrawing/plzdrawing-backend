package org.example.plzdrawing.domain.content.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.util.List;
import org.example.plzdrawing.domain.content.enums.TimeTaken;

public record ContentsDto(
        @Schema(description = "콘텐츠 id", example = "1")
        Long contentId,
        @Schema(description = "콘텐츠 생성일자", example = "2025-12-22")
        LocalDate createAt,
        @Schema(description = "콘텐츠 url", example = "[https://s3~, https://s3~]")
        List<String> contentUrl,
        @Schema(description = "콘텐츠 해시태그", example = "[사과, 바나나]")
        List<String> hashTag,
        @Schema(description = "콘텐츠 설명", example = "기린 그림입니다.")
        String explanation,
        @Schema(description = "소요 시간", example = "TEN,HALF_HOUR,MORE_THAN_AN_HOUR,DAY")
        TimeTaken timeTaken,
        @Schema(description = "가격", example = "1000")
        Long price,
        @Schema(description = "좋아요 수", example = "1")
        Long like
) {}
