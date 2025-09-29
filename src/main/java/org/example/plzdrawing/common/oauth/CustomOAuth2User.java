package org.example.plzdrawing.common.oauth;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.example.plzdrawing.common.oauth.dto.UserDTO;
import org.example.plzdrawing.domain.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

@RequiredArgsConstructor
public class CustomOAuth2User implements OAuth2User {
    private final UserDTO userDTO;
    @Override
    public Map<String, Object> getAttributes() {
        return Collections.emptyMap();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public String getName() {
        return userDTO.name();
    }

    public String getEmail() {
        return userDTO.email() != null ? userDTO.email() : "";
    }

    public Long getUserId() {
        return userDTO.userId();
    }

    public Role getRole() {
        return userDTO.role();
    }
}
