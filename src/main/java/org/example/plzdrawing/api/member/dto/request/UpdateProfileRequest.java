package org.example.plzdrawing.api.member.dto.request;


import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import org.example.plzdrawing.common.annotation.ValidHashTag;


public record UpdateProfileRequest(
        @Schema(description = "닉네임", example = "홍길동")
        String nickname,

        @Schema(description = "한 줄 소개", example = "그림 그리는 걸 좋아합니다.")
        String introduce,

        @Schema(description = "해시태그", example = "[해시태그1, 해시태그2]")
        @ValidHashTag List<String> hashTag
){}
