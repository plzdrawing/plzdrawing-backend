package org.example.plzdrawing.api.auth.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReissueResponse {
    @Schema(description = "액세스 토큰", example = "kefjdslf.dslfjsdfk.dslfsdlj")
    private String accessToken;
}
