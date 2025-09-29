package org.example.plzdrawing.common.oauth.dto;

import static org.example.plzdrawing.common.error.CommonErrorCode.OAUTH2_MISSING_EMAIL;
import static org.example.plzdrawing.common.error.CommonErrorCode.OAUTH2_MISSING_NICKNAME;

import io.micrometer.observation.annotation.Observed;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.example.plzdrawing.common.exception.RestApiException;
import org.example.plzdrawing.domain.member.Provider;

@RequiredArgsConstructor
public class KakaoResponse implements OAuth2Response{
    private final Map<String, Object> attributes;

    @Override
    public String getProvidedId() {
        Object id = attributes.get("id");
        return id != null ? id.toString() : null;
    }

    @Override
    public String getEmail() {
        Object kakaoAccountObj = attributes.get("kakao_account");
        if (kakaoAccountObj instanceof Map<?, ?> kakaoAccount) {
            Object email = kakaoAccount.get("email");
            if (email != null) {
                return email.toString();
            }
        }
        throw new RestApiException(OAUTH2_MISSING_EMAIL.getErrorCode());
    }

    @Override
    public String getName() {
        Object propertiesObj = attributes.get("properties");
        if (propertiesObj instanceof Map<?, ?> properties) {
            Object nickname = properties.get("nickname");
            if (nickname != null) {
                return nickname.toString();
            }
        }

        Object kakaoAccountObj = attributes.get("kakao_account");
        if (kakaoAccountObj instanceof Map<?, ?> kakaoAccount) {
            Object profileObj = kakaoAccount.get("profile");
            if (profileObj instanceof Map<?, ?> profile) {
                Object nickname = profile.get("nickname");
                if (nickname != null) {
                    return nickname.toString();
                }
            }
        }

        throw new RestApiException(OAUTH2_MISSING_NICKNAME.getErrorCode());
    }

    @Override
    public String getProfileImage() {
        Object kakaoAccountObj = attributes.get("kakao_account");
        if (kakaoAccountObj instanceof Map<?, ?> kakaoAccount) {
            Object profileObj = kakaoAccount.get("profile");
            if (profileObj instanceof Map<?, ?> profile) {
                Object image = profile.get("profile_image_url");
                if (image instanceof String) {
                    return (String) image;
                }
            }
        }
        return null;
    }

    public Provider getProvider() {
        return Provider.KAKAO;
    }
}
