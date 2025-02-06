package org.example.plzdrawing.api.auth.dto.request;

import lombok.Getter;

@Getter
public class UpdatePasswordRequest {

    private String email;
    private String password;
}
