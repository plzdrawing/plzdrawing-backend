package org.example.plzdrawing.api.auth.service.strategy;

import org.example.plzdrawing.api.auth.dto.request.LoginRequest;
import org.example.plzdrawing.api.auth.dto.request.SignUpRequest;
import org.example.plzdrawing.api.auth.dto.response.LoginResponse;
import org.example.plzdrawing.api.auth.dto.response.SignUpResponse;
import org.example.plzdrawing.domain.member.Provider;

public interface AuthService {

    LoginResponse login(LoginRequest request);

    SignUpResponse signUp(SignUpRequest request);

    Provider getProviderName();
}
