package org.example.plzdrawing.api.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.example.plzdrawing.common.annotation.ValidEmail;
import org.example.plzdrawing.common.annotation.ValidPassword;

@Getter
public class SignUpRequest {

    @ValidEmail
    @Schema(description = "이메일", example = "abc@def.com")
    private String email;

    @ValidPassword
    @Schema(description = "비밀번호", example = "1234")
    private String password;

    @Schema(description = "닉네임", example = "abc")
    private String nickName;

    @Schema(description = "약관동의(개인정보 수집 및 이용)", example = "true")
    private Boolean personalInfoConsent;

    @Schema(description = "이용정책 동의", example = "true")
    private Boolean acceptTermsOfUse;

    @Schema(description = "할인, 이벤트 소식 받기 동의", example = "false")
    private Boolean marketingConsent;
}
