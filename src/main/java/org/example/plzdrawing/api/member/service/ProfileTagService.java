package org.example.plzdrawing.api.member.service;

import java.util.List;
import org.example.plzdrawing.domain.member.Member;

public interface ProfileTagService {
    void syncMemberTags(Long memberId, List<String> hashTags);
}
