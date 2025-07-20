package org.example.plzdrawing.api.auth.dto.request;

import lombok.Getter;
import org.example.plzdrawing.common.annotation.ValidEmail;
import org.example.plzdrawing.common.annotation.ValidPassword;

@Getter
public class UpdatePasswordRequest {
    @ValidEmail
    private String email;

    @ValidPassword
    private String nowPassword;

    @ValidPassword
    private String newPassword;
}
