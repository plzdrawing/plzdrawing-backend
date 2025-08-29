package org.example.plzdrawing.api.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.example.plzdrawing.common.annotation.ValidEmail;

@Getter
public class CodeGenerateForPasswordRequest {
    @ValidEmail
    @Schema(description = "이메일", example = "abc@def.com")
    private String email;
}
