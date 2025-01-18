package org.example.plzdrawing.api.member.service;

import org.example.plzdrawing.domain.member.Member;
import org.example.plzdrawing.domain.member.Provider;

public interface MemberService {

    boolean isMemberExistsByEmailAndProvider(String email, Provider provider);

    Member findById(Long memberId);
}
