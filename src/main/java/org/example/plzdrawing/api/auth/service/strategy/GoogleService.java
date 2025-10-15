package org.example.plzdrawing.api.auth.service.strategy;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.plzdrawing.api.auth.customuser.CustomUser;
import org.example.plzdrawing.api.auth.dto.request.LoginRequest;
import org.example.plzdrawing.api.auth.dto.request.SignUpRequest;
import org.example.plzdrawing.api.auth.dto.response.LoginResponse;
import org.example.plzdrawing.api.auth.dto.response.SignUpResponse;
import org.example.plzdrawing.api.member.service.MemberService;
import org.example.plzdrawing.domain.member.Member;
import org.example.plzdrawing.domain.member.Provider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class GoogleService implements AuthService{
    private final MemberService memberService;

    @Override
    public Boolean login(LoginRequest request, HttpServletResponse response) {
        return null;
    }

    @Override
    public SignUpResponse signUp(CustomUser customUser, SignUpRequest request) {
        Member member = memberService.findById(Long.parseLong(customUser.getMember().getId().toString()));
        member.onboarding(request);
        return new SignUpResponse(member.getId());
    }

    @Override
    public Provider getProviderName() {
        return Provider.GOOGLE;
    }
}
