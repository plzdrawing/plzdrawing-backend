package org.example.plzdrawing.domain.content.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import org.example.plzdrawing.domain.content.enums.TimeTaken;

public record UploadContentRequest(
        @Schema(description = "설명", example = "게시글 설명")
        String explain,
        @Schema(description = "해시태그", example = "해시태그")
        List<String> hashTag,
        @Schema(description = "예상금액", example = "2000")
        Long price,
        @Schema(description = "예상 소요 시간", example = "TEN / HALF_HOUR / MORE_THAN_AN_HOUR / DAY")
        TimeTaken timeTaken
) {

}
