package org.example.plzdrawing.api.auth.dto.request;

import lombok.Getter;

@Getter
public class PasswordResetRequest {

    private String email;
    private String authCode;
}
