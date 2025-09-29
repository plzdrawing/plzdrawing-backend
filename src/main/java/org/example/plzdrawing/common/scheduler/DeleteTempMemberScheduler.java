package org.example.plzdrawing.common.scheduler;

import static org.example.plzdrawing.domain.Role.ROLE_TEMP;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.example.plzdrawing.domain.member.MemberRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class DeleteTempMemberScheduler {
    private final MemberRepository memberRepository;

    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void deleteTempMember() {
        LocalDateTime threshold = LocalDateTime.now().minusHours(24);
        memberRepository.deleteByEmailAndRoleBeforeThreshold(ROLE_TEMP, threshold);
    }
}
