package org.example.plzdrawing.api.member.service;

import java.util.List;
import org.example.plzdrawing.domain.member.Member;
import org.example.plzdrawing.domain.member.Provider;

public interface MemberService {

    boolean isMemberExistsByEmailAndProvider(String email, Provider provider);

    Member findById(Long memberId);

    List<Member> findMembersByIds(List<Long> memberIds);
}
