package org.example.plzdrawing.common.oauth.dto;

import org.example.plzdrawing.domain.Role;

public record UserDTO(
        Role role,
        String name,
        String email,
        Long userId
) {

}
