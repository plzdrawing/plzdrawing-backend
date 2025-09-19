package org.example.plzdrawing.common.oauth;

import static org.example.plzdrawing.common.error.CommonErrorCode.INVALID_OAUTH2_PROVIDER;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.example.plzdrawing.common.exception.RestApiException;
import org.example.plzdrawing.common.oauth.dto.KakaoResponse;
import org.example.plzdrawing.common.oauth.dto.OAuth2Response;
import org.example.plzdrawing.common.oauth.dto.UserDTO;
import org.example.plzdrawing.domain.Role;
import org.example.plzdrawing.domain.member.Member;
import org.example.plzdrawing.domain.member.MemberRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final MemberRepository memberRepository;

    @Transactional
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response = switch (registrationId) {
            case "kakao" -> new KakaoResponse(oAuth2User.getAttributes());
            default -> throw new RestApiException(INVALID_OAUTH2_PROVIDER.getErrorCode());
        };

        String email = oAuth2Response.getEmail();

        Optional<Member> existing = getMemberByEmail(email);
        if (existing.isPresent()) {
            Member member = existing.get();
            member.updateUser(email, registrationId, oAuth2Response.getName());

            Member savedMember = memberRepository.save(member);

            UserDTO userDto = new UserDTO(
                    oAuth2Response.getProvidedId(),
                    Role.ROLE_MEMBER,
                    oAuth2Response.getName(),
                    oAuth2Response.getEmail(),
                    oAuth2Response.getProfileImage(),
                    registrationId,
                    savedMember.getId()
            );
            return new CustomOAuth2User(userDto);
        } else {
            Member newMember = Member.createTempMember(
                    oAuth2Response.getEmail(),
                    oAuth2Response.getProvider()
            );

            Member savedMember = memberRepository.save(newMember);

            UserDTO userDto = new UserDTO(
                    oAuth2Response.getProvidedId(),
                    Role.ROLE_TEMP,
                    oAuth2Response.getName(),
                    oAuth2Response.getEmail(),
                    oAuth2Response.getProfileImage(),
                    registrationId,
                    savedMember.getId()
            );
            return new CustomOAuth2User(userDto);
        }
    }

    private Optional<Member> getMemberByEmail(String email) {
        return memberRepository.findByEmail(email);
    }
}
