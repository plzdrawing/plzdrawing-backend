package org.example.plzdrawing.api.auth.service.mail;

import static org.example.plzdrawing.common.aop.ratelimit.RateLimitFeature.*;
import static org.example.plzdrawing.domain.member.Provider.*;

import lombok.RequiredArgsConstructor;
import org.example.plzdrawing.api.auth.repository.AuthCodeRedisRepository;
import org.example.plzdrawing.api.member.exception.MemberErrorCode;
import org.example.plzdrawing.api.member.service.MemberService;
import org.example.plzdrawing.common.aop.ratelimit.RateLimit;
import org.example.plzdrawing.common.exception.RestApiException;
import org.example.plzdrawing.util.random.RandomGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EmailVerificationService implements MailService {

    private final AuthCodeRedisRepository authCodeRedisRepository;
    private final RandomGenerator randomGenerator;
    private final SendMailService sendMailService;
    private final MemberService memberService;

    @Transactional
    @RateLimit(feature = SIGN_UP)
    public void sendCodeEmail(String email) {
        if (isMemberExistsByEmailAndProvider(email)) {
            throw new RestApiException(MemberErrorCode.MEMBER_ALREADY_EXIST.getErrorCode());
        }

        String authNumber = randomGenerator.makeSecureRandomNumber();
        String title = "소일거리 드로잉 회원 가입 인증 메일입니다.";
        String content = "인증 번호는 " + authNumber + "입니다.";

        sendMailService.sendEmail(email, title, content);
        authCodeRedisRepository.saveAuthNumber(email, authNumber);
    }

    @Transactional
    @RateLimit(feature = RECOVERY_PASSWORD)
    public void sendEmailForRecoveryPassword(String email) {
        if (!isMemberExistsByEmailAndProvider(email)) {
            throw new RestApiException(MemberErrorCode.MEMBER_NOT_FOUND.getErrorCode());
        }

        String reissueAuthNumber = randomGenerator.makeSecureRandomNumber();
        String title = "소일거리 드로잉 비밀번호 재발급 인증 메일입니다.";
        String content = "인증 번호는 " + reissueAuthNumber + "입니다.";
        sendMailService.sendEmail(email, title, content);
        authCodeRedisRepository.saveReissueAuthNumber(email, reissueAuthNumber);
    }

    public void sendTemporaryPassword(String email, String password) {
        if (!isMemberExistsByEmailAndProvider(email)) {
            throw new RestApiException(MemberErrorCode.MEMBER_NOT_FOUND.getErrorCode());
        }

        String title = "소일거리 드로잉 임시 비밀번호 발급 안내드립니다.";
        String content = "새 비밀번호는 " + password + "입니다.";
        sendMailService.sendEmail(email, title, content);
    }

    private boolean isMemberExistsByEmailAndProvider(String email) {
        return memberService.isMemberExistsByEmailAndProvider(email, EMAIL);
    }
}
