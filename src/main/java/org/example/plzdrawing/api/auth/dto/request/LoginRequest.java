package org.example.plzdrawing.api.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.example.plzdrawing.common.annotation.ValidEmail;
import org.example.plzdrawing.common.annotation.ValidPassword;
import org.example.plzdrawing.domain.member.Provider;

@Getter
public class LoginRequest {
    @Schema(description = "제공자", example = "EMAIL / KAKAO / NAVER")
    private Provider provider;

    @ValidEmail
    @Schema(description = "이메일", example = "abc@def.com")
    private String email;

    @ValidPassword
    @Schema(description = "비밀번호", example = "1234")
    private String password;
}
