package org.example.plzdrawing.api.auth.dto.request;

import lombok.Getter;
import org.example.plzdrawing.domain.member.Provider;

@Getter
public class LoginRequest {

    private Provider provider;
    private String email;
    private String password;
}
