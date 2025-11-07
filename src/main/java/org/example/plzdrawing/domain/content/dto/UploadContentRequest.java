package org.example.plzdrawing.domain.content.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

public record UploadContentRequest(
        @Schema(description = "설명", example = "게시글 설명")
        String explain,
        @Schema(description = "해시태그", example = "해시태그")
        List<String> hashTag
) {

}
