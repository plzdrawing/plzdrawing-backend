package org.example.plzdrawing.api.auth.service.strategy;

import jakarta.servlet.http.HttpServletResponse;
import org.example.plzdrawing.api.auth.customuser.CustomUser;
import org.example.plzdrawing.api.auth.dto.request.LoginRequest;
import org.example.plzdrawing.api.auth.dto.request.SignUpRequest;
import org.example.plzdrawing.api.auth.dto.response.SignUpResponse;
import org.example.plzdrawing.domain.member.Provider;

public interface AuthService {

    Boolean login(LoginRequest request, HttpServletResponse response);

    SignUpResponse signUp(CustomUser customUser, SignUpRequest request);

    Provider getProviderName();
}
