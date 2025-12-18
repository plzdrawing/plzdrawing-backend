package org.example.plzdrawing.api.member.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import java.util.List;
import org.example.plzdrawing.common.annotation.ValidHashTag;

public record UpsertProfileRequest(
        @Schema(description = "한 줄 소개", example = "안녕하십니까?")
        @Size(max = 30, message = "소개는 30자 이내로 입력해주세요.")
        String introduce,
        @Schema(description = "해시태그", example = "[해시태그1, 해시태그2]")
        @ValidHashTag List<String> hashTag
) {

}
