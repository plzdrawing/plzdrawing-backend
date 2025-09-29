package org.example.plzdrawing.common.oauth.dto;

import org.example.plzdrawing.domain.member.Provider;

public interface OAuth2Response {
    String getProvidedId();
    String getEmail();
    String getName();
    String getProfileImage();

    Provider getProvider();
}
