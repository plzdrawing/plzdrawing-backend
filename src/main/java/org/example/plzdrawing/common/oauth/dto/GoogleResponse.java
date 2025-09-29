package org.example.plzdrawing.common.oauth.dto;

import static org.example.plzdrawing.common.error.CommonErrorCode.OAUTH2_MISSING_EMAIL;
import static org.example.plzdrawing.common.error.CommonErrorCode.OAUTH2_MISSING_NICKNAME;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.example.plzdrawing.common.exception.RestApiException;
import org.example.plzdrawing.domain.member.Provider;

@RequiredArgsConstructor
public class GoogleResponse implements OAuth2Response{
    private final Map<String, Object> attributes;

    @Override
    public String getProvidedId() {
        Object sub = attributes.get("sub");
        return sub != null ? sub.toString() : null;
    }

    @Override
    public String getEmail() {
        Object email = attributes.get("email");
        if (email != null) {
            return email.toString();
        }
        throw new RestApiException(OAUTH2_MISSING_EMAIL.getErrorCode());
    }

    @Override
    public String getName() {
        // 우선순위: name -> given_name + family_name -> nickname (거의 없음)
        Object name = attributes.get("name");
        if (name != null) return name.toString();

        Object given = attributes.get("given_name");
        Object family = attributes.get("family_name");
        if (given != null || family != null) {
            String g = given != null ? given.toString() : "";
            String f = family != null ? family.toString() : "";
            String combined = (g + " " + f).trim();
            if (!combined.isEmpty()) return combined;
        }

        Object nickname = attributes.get("nickname");
        if (nickname != null) return nickname.toString();

        throw new RestApiException(OAUTH2_MISSING_NICKNAME.getErrorCode());
    }

    @Override
    public String getProfileImage() {
        Object picture = attributes.get("picture");
        return picture != null ? picture.toString() : null;
    }

    @Override
    public Provider getProvider() {
        return Provider.GOOGLE;
    }
}
