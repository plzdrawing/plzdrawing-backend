package org.example.plzdrawing.api.auth.dto.request;

import lombok.Getter;
import org.example.plzdrawing.common.annotation.ValidEmail;

@Getter
public class PasswordResetRequest {
    @ValidEmail
    private String email;
    private String authCode;
}
