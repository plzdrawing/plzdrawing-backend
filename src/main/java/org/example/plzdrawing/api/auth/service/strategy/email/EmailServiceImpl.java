package org.example.plzdrawing.api.auth.service.strategy.email;

import static org.example.plzdrawing.api.auth.exception.AuthErrorCode.AUTH_CODE_INCORRECT;
import static org.example.plzdrawing.api.auth.exception.AuthErrorCode.EXIST_EMAIL;
import static org.example.plzdrawing.domain.Role.ROLE_MEMBER;
import static org.example.plzdrawing.domain.Role.ROLE_TEMP;

import org.example.plzdrawing.api.auth.customuser.CustomUser;
import org.example.plzdrawing.api.member.exception.MemberErrorCode;

import lombok.RequiredArgsConstructor;
import org.example.plzdrawing.api.auth.dto.request.LoginRequest;
import org.example.plzdrawing.api.auth.dto.request.SignUpRequest;
import org.example.plzdrawing.api.auth.dto.response.LoginResponse;
import org.example.plzdrawing.api.auth.dto.response.SignUpResponse;
import org.example.plzdrawing.api.auth.repository.AuthCodeRedisRepository;
import org.example.plzdrawing.api.auth.service.mail.MailService;
import org.example.plzdrawing.api.member.service.MemberService;
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

    private final MemberService memberService;
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

        validatePassword(request.getPassword(), member);

        String accessToken = tokenService.createAccessToken(String.valueOf(member.getId()), ROLE_MEMBER);
        String refreshToken = tokenService.createRefreshToken(String.valueOf(member.getId()));

        return new LoginResponse(accessToken, refreshToken);
    }

    @Transactional
    @Override
    public SignUpResponse signUp(CustomUser customUser, SignUpRequest request) {
        Member member = memberService.findById(Long.parseLong(customUser.getMember().getId().toString()));
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        member.onboarding(request);
        member.updatePassword(encodedPassword);
        return new SignUpResponse(member.getId());
    }

    @Override
    public Provider getProviderName() {
        return Provider.EMAIL;
    }

    @Transactional
    @Override
    public void sendCode(String email) {
        if(memberRepository.findByRoleAndProviderAndEmail(ROLE_MEMBER, Provider.EMAIL, email).isPresent()) {
            throw new RestApiException(EXIST_EMAIL.getErrorCode());
        }
        mailService.sendCodeEmail(email);
    }

    @Transactional
    @Override
    public String verifyAuthCode(String email, String code) {
        String savedCode = authCodeRedisRepository.findEmailAuthNumberByKey(email);

        if (!isMatchingCode(code, savedCode)) {
            throw new RestApiException(AUTH_CODE_INCORRECT.getErrorCode());
        }

        Member member = memberRepository.save(Member.createTempMember(email, Provider.EMAIL));
        return tokenService.createAccessToken(member.getId().toString(), ROLE_TEMP);
    }

    @Transactional
    @Override
    public void sendEmailForRecoveryPassword(String email) {
        mailService.sendEmailForRecoveryPassword(email);
    }

    @Transactional
    public Boolean reissuePassword(String email, String authCode) {
        if (!verifyReissueAuthCode(email, authCode)) {
            throw new RestApiException(AUTH_CODE_INCORRECT.getErrorCode());
        }
        String password = randomGenerator.generateTemporaryPassword();
        savePassword(email, password);
        mailService.sendTemporaryPassword(email, password);
        return true;
    }

    @Transactional
    @Override
    public void changePassword(String email, String password, String newPassword) {
        Member member = memberRepository.findByEmailAndProvider(email, Provider.EMAIL)
                .orElseThrow(() -> new RestApiException(MemberErrorCode.MEMBER_NOT_FOUND.getErrorCode()));
        validatePassword(password, member);

        savePassword(email, newPassword);
    }

    private void savePassword(String email, String password) {
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

    private void validatePassword(String inputPassword, Member member) {
        if (!isPasswordMatching(inputPassword, member.getPassword())) {
            throw new RestApiException(MemberErrorCode.PASSWORD_INCORRECT.getErrorCode());
        }
    }

    private boolean isPasswordMatching(String inputPassword, String savedPassword) {
        return passwordEncoder.matches(inputPassword, savedPassword);
    }
}
