package org.example.plzdrawing.api.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.example.plzdrawing.common.annotation.ValidEmail;
import org.example.plzdrawing.common.annotation.ValidPassword;

@Getter
public class UpdatePasswordRequest {

    @ValidPassword
    @Schema(description = "현재 비밀번호", example = "Test1234!")
    private String nowPassword;

    @ValidPassword
    @Schema(description = "새 비밀번호", example = "Test1234!")
    private String newPassword;
}
