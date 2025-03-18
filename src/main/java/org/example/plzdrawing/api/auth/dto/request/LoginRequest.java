package org.example.plzdrawing.api.auth.dto.request;

import lombok.Getter;
import org.example.plzdrawing.common.annotation.ValidEmail;
import org.example.plzdrawing.common.annotation.ValidPassword;
import org.example.plzdrawing.domain.member.Provider;

@Getter
public class LoginRequest {

    private Provider provider;

    @ValidEmail
    private String email;

    @ValidPassword
    private String password;
}
