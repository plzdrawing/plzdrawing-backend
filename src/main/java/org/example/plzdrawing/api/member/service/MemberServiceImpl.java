package org.example.plzdrawing.api.member.service;

import static org.example.plzdrawing.api.member.exception.MemberErrorCode.MEMBER_NOT_FOUND;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.plzdrawing.common.exception.RestApiException;
import org.example.plzdrawing.domain.member.Member;
import org.example.plzdrawing.domain.member.MemberRepository;
import org.example.plzdrawing.domain.member.Provider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Override
    public boolean isMemberExistsByEmailAndProvider(String email, Provider provider) {
        return memberRepository.findByEmailAndProvider(email, provider).isPresent();
    }

    public Member findById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(()->new RestApiException(MEMBER_NOT_FOUND.getErrorCode()));
    }

    public List<Member> findMembersByIds(List<Long> memberIds) {
        return memberRepository.findByIdIn(memberIds);
    }
}
