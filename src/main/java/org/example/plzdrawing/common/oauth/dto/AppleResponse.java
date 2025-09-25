package org.example.plzdrawing.common.oauth.dto;

import static org.example.plzdrawing.common.error.CommonErrorCode.OAUTH2_MISSING_EMAIL;
import static org.example.plzdrawing.common.error.CommonErrorCode.OAUTH2_MISSING_NICKNAME;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.example.plzdrawing.common.exception.RestApiException;
import org.example.plzdrawing.domain.member.Provider;

@RequiredArgsConstructor
public class AppleResponse implements OAuth2Response{
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
        // 애플은 최초 로그인 시에만 name 내려줌. 이후에는 null일 수 있음.
        Object name = attributes.get("name");
        if (name != null) {
            return name.toString();
        }
        // fallback: email 앞부분을 이름처럼 활용
        Object email = attributes.get("email");
        if (email != null) {
            return email.toString().split("@")[0];
        }
        throw new RestApiException(OAUTH2_MISSING_NICKNAME.getErrorCode());
    }

    @Override
    public String getProfileImage() {
        return null;
    }

    @Override
    public Provider getProvider() {
        return Provider.APPLE;
    }
}
