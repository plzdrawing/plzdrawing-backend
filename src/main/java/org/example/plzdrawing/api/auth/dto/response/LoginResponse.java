package org.example.plzdrawing.api.auth.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponse {
    @Schema(description = "액세스 토큰", example = "kefjdslf.dslfjsdfk.dslfsdlj")
    private String accessToken;
    @Schema(description = "리프레쉬 토큰", example = "kefjdslf.dslfjsdfk.dslfsdlj")
    private String refreshToken;
}
