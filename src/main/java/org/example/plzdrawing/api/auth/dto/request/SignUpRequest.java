package org.example.plzdrawing.api.auth.dto.request;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import org.example.plzdrawing.common.annotation.ValidEmail;
import org.example.plzdrawing.common.annotation.ValidPassword;
import org.example.plzdrawing.domain.member.Provider;
import org.hibernate.validator.constraints.Length;

@Getter
public class SignUpRequest {

    private Provider provider;

    @ValidEmail
    private String email;

    @ValidPassword
    private String password;

    private String nickName;
}
