package org.example.plzdrawing.api.member.service;

import org.example.plzdrawing.api.member.dto.response.ProfileInfoResponse;
import org.example.plzdrawing.domain.Profile;
import org.example.plzdrawing.api.member.dto.request.UpdateProfileRequest;
import org.example.plzdrawing.api.member.dto.response.ProfileResponse;
import org.example.plzdrawing.domain.member.Member;
import org.example.plzdrawing.domain.member.Provider;

public interface MemberService {

    boolean isMemberExistsByEmailAndProvider(String email, Provider provider);

    Member findById(Long memberId);

    Profile makeProfile(Member member, String fileName, String introduce);
    ProfileResponse updateProfile(Long memberId, UpdateProfileRequest request);
    ProfileInfoResponse getMyProfile(Long memberId);


}
