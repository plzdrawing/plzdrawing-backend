package org.example.plzdrawing.api.auth.service.strategy;

import lombok.RequiredArgsConstructor;
import org.example.plzdrawing.api.auth.customuser.CustomUser;
import org.example.plzdrawing.api.auth.dto.request.LoginRequest;
import org.example.plzdrawing.api.auth.dto.request.SignUpRequest;
import org.example.plzdrawing.api.auth.dto.response.LoginResponse;
import org.example.plzdrawing.api.auth.dto.response.SignUpResponse;
import org.example.plzdrawing.domain.member.Provider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class NaverService implements AuthService{

    @Override
    public LoginResponse login(LoginRequest request) {
        return null;
    }

    @Override
    public SignUpResponse signUp(CustomUser customUser, SignUpRequest request) {
        return null;
    }

    @Override
    public Provider getProviderName() {
        return Provider.NAVER;
    }
}
