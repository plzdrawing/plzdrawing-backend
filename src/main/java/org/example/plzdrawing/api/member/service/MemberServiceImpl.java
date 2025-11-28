package org.example.plzdrawing.api.member.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import org.example.plzdrawing.api.member.dto.request.UpdateProfileRequest;
import org.example.plzdrawing.api.member.dto.response.ProfileInfoResponse;
import org.example.plzdrawing.api.member.dto.response.ProfileResponse;
import org.example.plzdrawing.common.cookie.CookieService;
import org.example.plzdrawing.common.exception.RestApiException;
import org.example.plzdrawing.domain.MemberTag;
import org.example.plzdrawing.domain.Profile;
import org.example.plzdrawing.domain.ProfileRepository;
import org.example.plzdrawing.domain.Tag;
import org.example.plzdrawing.domain.member.Member;
import org.example.plzdrawing.domain.member.MemberRepository;
import org.example.plzdrawing.domain.member.Provider;
import org.example.plzdrawing.domain.Status;
import org.example.plzdrawing.util.jwt.JwtTokenProvider;
import org.example.plzdrawing.util.s3.S3Service;
import org.springframework.stereotype.Service;

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
    private final S3Service s3Service;

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
        Member member = memberRepository.findWithProfileAndTags(memberId)
                .orElseThrow(() -> new RestApiException(MEMBER_NOT_FOUND.getErrorCode()));

        Profile profile = member.getProfile();

        String introduce = null;
        String profileUrl = null;

        if (profile != null) {
            introduce = profile.getIntroduce();
            profileUrl = s3Service.getFileUrl(profile.getProfileUrl());
        }

        List<String> tags = new ArrayList<>(member.getMemberTags().stream()
                .filter(mt -> mt.getStatus() == Status.ACTIVE)
                .map(mt -> mt.getTag().getContent())
                .toList());

        if (member.getMemberTags() != null) {
            for (MemberTag mt : member.getMemberTags()) {
                Tag tag = mt.getTag();
                if (tag != null) {
                    tags.add(tag.getContent());
                }
            }
        }

        return new ProfileInfoResponse(
                member.getNickname(),
                introduce,
                tags,
                profileUrl
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
