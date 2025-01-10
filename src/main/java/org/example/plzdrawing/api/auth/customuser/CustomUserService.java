package org.example.plzdrawing.api.auth.customuser;


import lombok.RequiredArgsConstructor;
import org.example.plzdrawing.api.member.service.MemberService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomUserService implements UserDetailsService {

    private final MemberService memberService;

    @Override
    @Transactional(readOnly = true)
    public CustomUser loadUserByUsername(String username) throws UsernameNotFoundException {
        memberService.findById(Long.valueOf(username));
        return new CustomUser(username);
    }
}
