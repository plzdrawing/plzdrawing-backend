package org.example.plzdrawing.api.auth.service.strategy;

import static org.example.plzdrawing.api.member.exception.MemberErrorCode.MEMBER_NOT_FOUND;
import static org.example.plzdrawing.api.member.exception.MemberErrorCode.PASSWORD_INCORRECT;

import lombok.RequiredArgsConstructor;
import org.example.plzdrawing.api.auth.dto.request.LoginRequest;
import org.example.plzdrawing.api.auth.dto.request.SignUpRequest;
import org.example.plzdrawing.api.auth.dto.response.LoginResponse;
import org.example.plzdrawing.api.auth.dto.response.SignUpResponse;
import org.example.plzdrawing.common.exception.RestApiException;
import org.example.plzdrawing.domain.member.Member;
import org.example.plzdrawing.domain.member.MemberRepository;
import org.example.plzdrawing.domain.member.Provider;
import org.example.plzdrawing.util.jwt.TokenService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class EmailService implements AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    @Override
    public LoginResponse login(LoginRequest request) {
        Member member = memberRepository.findByEmailAndProvider(request.getEmail(),
                request.getProvider()).orElseThrow(()->new RestApiException(MEMBER_NOT_FOUND.getErrorCode()));

        validatePassword(request, member);

        String accessToken = tokenService.createAccessToken(String.valueOf(member.getId()));
        String refreshToken = tokenService.createRefreshToken(String.valueOf(member.getId()));

        return new LoginResponse(accessToken, refreshToken);
    }

    @Override
    public SignUpResponse signUp(SignUpRequest request) {
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        Member member = Member.builder()
                .email(request.getEmail())
                .password(encodedPassword)
                .provider(request.getProvider())
                .nickname(request.getNickName())
                .build();

        Long savedId = memberRepository.save(member).getId();
        return new SignUpResponse(savedId);
    }

    @Override
    public Provider getProviderName() {
        return Provider.EMAIL;
    }

    private void validatePassword(LoginRequest request, Member member) {
        if (!isPasswordMatching(request.getPassword(), member.getPassword())) {
            throw new RestApiException(PASSWORD_INCORRECT.getErrorCode());
        }
    }

    private boolean isPasswordMatching(String inputPassword, String savedPassword) {
        return passwordEncoder.matches(inputPassword, savedPassword);
    }
}
