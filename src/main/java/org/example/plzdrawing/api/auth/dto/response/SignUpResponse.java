package org.example.plzdrawing.api.auth.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SignUpResponse {
    @Schema(description = "멤버 식별자", example = "1")
    private Long memberId;
}
