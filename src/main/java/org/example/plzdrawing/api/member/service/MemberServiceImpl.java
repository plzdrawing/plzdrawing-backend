package org.example.plzdrawing.api.member.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.plzdrawing.api.member.dto.request.UpdateProfileRequest;
import org.example.plzdrawing.api.member.dto.response.ProfileInfoResponse;
import org.example.plzdrawing.api.member.dto.response.ProfileResponse;
import org.example.plzdrawing.common.cookie.CookieService;
import org.example.plzdrawing.common.exception.RestApiException;
import org.example.plzdrawing.domain.Profile;
import org.example.plzdrawing.domain.ProfileRepository;
import org.example.plzdrawing.domain.member.Member;
import org.example.plzdrawing.domain.member.MemberRepository;
import org.example.plzdrawing.domain.member.Provider;
import org.example.plzdrawing.util.jwt.JwtTokenProvider;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

import static org.example.plzdrawing.api.member.exception.MemberErrorCode.MEMBER_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final ProfileRepository profileRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final CookieService cookieService;

    @Override
    public boolean isMemberExistsByEmailAndProvider(String email, Provider provider) {
        return memberRepository.findByEmailAndProvider(email, provider).isPresent();
    }

    @Override
    public Member findById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new RestApiException(MEMBER_NOT_FOUND.getErrorCode()));
    }


    public Profile makeProfile(Member member, String fileName, String introduce) {
        Profile profile = profileRepository.findByMemberId(member.getId())
                .orElseGet(() -> Profile.builder()
                        .member(member)
                        .build());

        profile.updateProfileUrl(fileName);
        profile.updateIntroduce(introduce);

        return profileRepository.save(profile);
    }

    public ProfileResponse updateProfile(Long memberId, UpdateProfileRequest request) {
        Member member = findById(memberId);

        member.updateProfile(
                request.getNickname(),
                request.getIntroduction(),
                request.getHashtags(),
                request.getProfileImageUrl()
        );

        return new ProfileResponse(
                member.getNickname(),
                member.getIntroduction(),
                member.getHashtags(),
                member.getProfileImageUrl()
        );
    }

    @Override
    public ProfileInfoResponse getMyProfile(Long memberId) {
        Member member = findById(memberId);

        List<String> hashtagList = member.getHashtags() != null
                ? Arrays.asList(member.getHashtags().replace("#", "").split(" "))
                : List.of();

        return new ProfileInfoResponse(
                member.getNickname(),
                member.getIntroduction(),
                hashtagList,
                member.getProfileImageUrl()
        );
    }

    @Override
    public void withdraw(Long memberId, HttpServletRequest request, HttpServletResponse response) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RestApiException(MEMBER_NOT_FOUND.getErrorCode()));

        jwtTokenProvider.deleteRefreshToken(String.valueOf(memberId));

        cookieService.expireCookie("access_token", response);
        cookieService.expireCookie("refresh_token", response);

        memberRepository.delete(member);

        profileRepository.findByMemberId(memberId).ifPresent(profileRepository::delete);
    }
}
