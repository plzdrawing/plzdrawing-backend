package org.example.plzdrawing.common.scheduler;

import lombok.RequiredArgsConstructor;
import org.example.plzdrawing.domain.member.MemberRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeleteTempMemberScheduler {
    private final MemberRepository memberRepository;


}
