package org.example.plzdrawing.domain.content.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record UploadContentResponse(
        @Schema(description = "contentId", example = "1")
        Long contentId
) {
    public static UploadContentResponse of(
            Long contentId
    ) {
        return new UploadContentResponse(
                contentId
        );
    }
}
