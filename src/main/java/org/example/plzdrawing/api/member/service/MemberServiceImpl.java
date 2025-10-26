package org.example.plzdrawing.api.member.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.plzdrawing.api.member.dto.request.UpdateProfileRequest;
import org.example.plzdrawing.api.member.dto.response.ProfileResponse;
import org.example.plzdrawing.common.exception.RestApiException;
import org.example.plzdrawing.domain.Profile;
import org.example.plzdrawing.domain.ProfileRepository;
import org.example.plzdrawing.domain.member.Member;
import org.example.plzdrawing.domain.member.MemberRepository;
import org.example.plzdrawing.domain.member.Provider;
import org.springframework.stereotype.Service;

import static org.example.plzdrawing.api.member.exception.MemberErrorCode.MEMBER_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final ProfileRepository profileRepository;

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
}
