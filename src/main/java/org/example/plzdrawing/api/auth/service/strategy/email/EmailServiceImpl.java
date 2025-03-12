package org.example.plzdrawing.api.auth.service.strategy.email;

import org.example.plzdrawing.api.member.exception.MemberErrorCode;

import lombok.RequiredArgsConstructor;
import org.example.plzdrawing.api.auth.dto.request.LoginRequest;
import org.example.plzdrawing.api.auth.dto.request.SignUpRequest;
import org.example.plzdrawing.api.auth.dto.response.LoginResponse;
import org.example.plzdrawing.api.auth.dto.response.SignUpResponse;
import org.example.plzdrawing.api.auth.exception.AuthErrorCode;
import org.example.plzdrawing.api.auth.repository.AuthCodeRedisRepository;
import org.example.plzdrawing.api.auth.service.mail.MailService;
import org.example.plzdrawing.common.exception.RestApiException;
import org.example.plzdrawing.domain.member.Member;
import org.example.plzdrawing.domain.member.MemberRepository;
import org.example.plzdrawing.domain.member.Provider;
import org.example.plzdrawing.util.jwt.TokenService;
import org.example.plzdrawing.util.random.RandomGenerator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final AuthCodeRedisRepository authCodeRedisRepository;
    private final MailService mailService;
    private final RandomGenerator randomGenerator;

    @Transactional
    @Override
    public LoginResponse login(LoginRequest request) {
        Member member = memberRepository.findByEmailAndProvider(request.getEmail(),
                request.getProvider()).orElseThrow(()->new RestApiException(MemberErrorCode.MEMBER_NOT_FOUND.getErrorCode()));

        validatePassword(request, member);

        String accessToken = tokenService.createAccessToken(String.valueOf(member.getId()));
        String refreshToken = tokenService.createRefreshToken(String.valueOf(member.getId()));

        return new LoginResponse(accessToken, refreshToken);
    }

    @Transactional
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

    @Transactional
    @Override
    public void sendCode(String email) {
        mailService.sendCodeEmail(email);
    }

    @Transactional(readOnly = true)
    @Override
    public Boolean verifyAuthCode(String email, String code) {
        String savedCode = authCodeRedisRepository.findEmailAuthNumberByKey(email);
        return isMatchingCode(code, savedCode);
    }

    @Transactional
    @Override
    public void sendEmailForRecoveryPassword(String email) {
        mailService.sendEmailForRecoveryPassword(email);
    }

    @Transactional
    public Boolean reissuePassword(String email, String authCode) {
        if (!verifyReissueAuthCode(email, authCode)) {
            throw new RestApiException(AuthErrorCode.AUTH_CODE_INCORRECT.getErrorCode());
        }
        String password = randomGenerator.generateTemporaryPassword();
        updatePassword(email, password);
        return true;
    }

    @Override
    public void updatePassword(String email, String password) {
        password = passwordEncoder.encode(password);

        Member member = memberRepository.findByEmailAndProvider(email, Provider.EMAIL)
                .orElseThrow(() -> new RestApiException(MemberErrorCode.MEMBER_NOT_FOUND.getErrorCode()));

        member.updatePassword(password);
    }

    private boolean verifyReissueAuthCode(String email, String reissueAuthCode) {
        String savedCode = authCodeRedisRepository.findReissueAuthNumberByKey(email);
        return isMatchingCode(reissueAuthCode, savedCode);
    }


    private boolean isMatchingCode(String code, String savedCode) {
        return code.equals(savedCode);
    }

    private void validatePassword(LoginRequest request, Member member) {
        if (!isPasswordMatching(request.getPassword(), member.getPassword())) {
            throw new RestApiException(MemberErrorCode.PASSWORD_INCORRECT.getErrorCode());
        }
    }

    private boolean isPasswordMatching(String inputPassword, String savedPassword) {
        return passwordEncoder.matches(inputPassword, savedPassword);
    }
}
