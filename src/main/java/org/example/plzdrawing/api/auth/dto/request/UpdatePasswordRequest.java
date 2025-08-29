package org.example.plzdrawing.api.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.example.plzdrawing.common.annotation.ValidEmail;
import org.example.plzdrawing.common.annotation.ValidPassword;

@Getter
public class UpdatePasswordRequest {
    @ValidEmail
    @Schema(description = "이메일", example = "abc@cdf.com")
    private String email;

    @ValidPassword
    @Schema(description = "현재 비밀번호", example = "1234")
    private String nowPassword;

    @ValidPassword
    @Schema(description = "새 비밀번호", example = "1235")
    private String newPassword;
}
