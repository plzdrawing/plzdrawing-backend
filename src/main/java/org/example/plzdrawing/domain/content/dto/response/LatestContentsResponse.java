package org.example.plzdrawing.domain.content.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.example.plzdrawing.api.member.dto.response.UploaderDto;

public record LatestContentsResponse(
        @Schema(description = "업로더 정보")
        UploaderDto uploaderDto,
        @Schema(description = "콘텐츠 정보")
        ContentsDto contentsDto
) {

}
