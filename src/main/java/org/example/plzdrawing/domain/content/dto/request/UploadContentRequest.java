package org.example.plzdrawing.domain.content.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import org.example.plzdrawing.common.annotation.ValidHashTag;
import org.example.plzdrawing.domain.content.enums.TimeTaken;

public record UploadContentRequest(
        @Schema(description = "제목", example = "그림 제목")
        String title,
        @Schema(description = "설명", example = "게시글 설명")
        String explain,
        @Schema(description = "해시태그", example = "[해시태그1, 해시태그2]")
        @ValidHashTag(message = "해시태그는 5개 이하, 10자 이하, 특수문자 금지입니다.")
        List<String> hashTag,
        @Schema(description = "예상금액", example = "2000")
        Long price,
        @Schema(description = "예상 소요 시간", example = "TEN / HALF_HOUR / MORE_THAN_AN_HOUR / DAY")
        TimeTaken timeTaken
) {

}
