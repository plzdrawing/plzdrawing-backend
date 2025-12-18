package org.example.plzdrawing.domain.content.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Size;
import java.util.List;
import org.example.plzdrawing.common.annotation.ValidHashTag;
import org.example.plzdrawing.domain.content.enums.TimeTaken;

public record UpdateContentRequest(
        @Schema(description = "수정할 게시글 Id", example = "1")
        Long contentId,
        @Schema(description = "수정할 게시글 제목", example = "그림 제목")
        @Size(max = 20, message = "그림제목은 20자 이내로 입력해주세요.")
        String title,
        @Schema(description = "설명", example = "게시글 설명")
        @Size(max = 30, message = "그림설명은 30자 이내로 입력해주세요.")
        String explain,
        @Schema(description = "해시태그", example = "[해시태그1, 해시태그2]")
        @ValidHashTag(message = "해시태그는 5개 이하, 10자 이하, 특수문자 금지입니다.")
        List<String> hashTag,
        @Schema(description = "예상금액", example = "2000")
        @Digits(integer = 10, fraction = 0, message = "예상금액은 최대 10자리 정수만 입력 가능합니다.")
        Long price,
        @Schema(description = "예상 소요 시간", example = "TEN / HALF_HOUR / MORE_THAN_AN_HOUR / DAY")
        TimeTaken timeTaken
) {

}
