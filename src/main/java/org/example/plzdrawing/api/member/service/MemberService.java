package org.example.plzdrawing.api.member.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import org.example.plzdrawing.api.member.dto.response.ProfileInfoResponse;
import org.example.plzdrawing.domain.Profile;
import org.example.plzdrawing.domain.member.Member;
import org.example.plzdrawing.domain.member.Provider;

public interface MemberService {

    boolean isMemberExistsByEmailAndProvider(String email, Provider provider);

    Member findById(Long memberId);
    Profile upsertProfile(Member member, String fileName, String introduce);
    ProfileInfoResponse getMyProfile(Long memberId);
    void withdraw(Long memberId, HttpServletRequest request, HttpServletResponse response);
    Map<Long, String> findProfileImageUrlByMemberIds(List<Long> memberIds);
}
