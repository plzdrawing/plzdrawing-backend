package org.example.plzdrawing.common.oauth.dto;

import org.example.plzdrawing.domain.Role;

public record UserDTO(
        String registrationId,
        Role role,
        String name,
        String username,
        String email,
        String profileImage,
        Long userId
) {

}
