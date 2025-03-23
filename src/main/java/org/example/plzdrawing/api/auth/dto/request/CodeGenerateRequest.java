package org.example.plzdrawing.api.auth.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.plzdrawing.common.annotation.ValidEmail;

@Getter
@NoArgsConstructor
public class CodeGenerateRequest {
    @ValidEmail
    private String email;
}
