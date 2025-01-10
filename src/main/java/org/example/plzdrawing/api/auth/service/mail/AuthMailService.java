package org.example.plzdrawing.api.auth.service.mail;

import static org.example.plzdrawing.domain.member.Provider.*;

import lombok.RequiredArgsConstructor;
import org.example.plzdrawing.api.auth.dto.response.EmailVerificationResponse;
import org.example.plzdrawing.api.auth.repository.AuthCodeRedisRepository;
import org.example.plzdrawing.api.member.exception.MemberErrorCode;
import org.example.plzdrawing.api.member.service.MemberService;
import org.example.plzdrawing.common.exception.RestApiException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthMailService implements MailService {

    private final AuthCodeRedisRepository authCodeRedisRepository;
    private final RandomGenerator randomGenerator;
    private final SendMailService sendMailService;
    private final MemberService memberService;

    @Transactional
    public EmailVerificationResponse sendCodeEmail(String email) {
        if (isMemberExistsByEmailAndProvider(email)) {
            throw new RestApiException(MemberErrorCode.MEMBER_ALREADY_EXIST.getErrorCode());
        }

        //TODO 자주 보내기 방지
        String authNumber = randomGenerator.makeSecureRandomNumber();
        String title = "소일거리 드로잉 회원 가입 인증 메일입니다.";
        String content = "인증 번호는 " + authNumber + "입니다.";

        sendMailService.sendEmail(email, title, content);
        authCodeRedisRepository.saveAuthNumber(email, authNumber);

        return new EmailVerificationResponse(email);
    }

    public boolean verifyAuthCode(String email, String code) {
        String savedCode = authCodeRedisRepository.findEmailAuthNumberByKey(email);
        return isMatchingCode(code, savedCode);
    }

    private boolean isMatchingCode(String code, String savedCode) {
        return code.equals(savedCode);
    }

    private boolean isMemberExistsByEmailAndProvider(String email) {
        return memberService.isMemberExistsByEmailAndProvider(email, EMAIL);
    }
}
