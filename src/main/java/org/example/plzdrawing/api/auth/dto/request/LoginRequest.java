package org.example.plzdrawing.api.auth.dto.request;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import org.example.plzdrawing.common.annotation.ValidEmail;
import org.example.plzdrawing.domain.member.Provider;
import org.hibernate.validator.constraints.Length;

@Getter
public class LoginRequest {

    private Provider provider;

    @ValidEmail
    private String email;

    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]).*$", message = "영문, 숫자, 특수문자 혼합")
    @Length(min = 5, max = 20, message = "8 ~ 20자리 이내")
    private String password;
}
