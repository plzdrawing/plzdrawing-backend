package org.example.plzdrawing.api.member.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record UploaderDto(
        @Schema(description = "닉네임", example = "홍길동")
        String nickname,
        @Schema(description = "프로필 이미지", example = "https://s3~")
        String profileImageUrl,
        @Schema(description = "그림 횟수", example = "1")
        Long drawingCount,
        @Schema(description = "후기 개수", example = "1")
        Long reviewCount,
        @Schema(description = "별점", example = "3.5")
        float star
) {

}
