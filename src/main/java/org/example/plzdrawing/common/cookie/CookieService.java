package org.example.plzdrawing.common.cookie;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

@Service
public class CookieService {
    @Value("${server.reactive.session.cookie.domain}")
    String cookieDomain;
    @Value("${server.reactive.session.cookie.secure}")
    Boolean secure;
    @Value("${server.reactive.session.cookie.http-only}")
    Boolean httpOnly;
    @Value("${server.reactive.session.cookie.same-site}")
    String sameSite;

    public ResponseCookie createCookie(String key, String value, Long maxAge) {
        return ResponseCookie.from(key, value)
                .path("/")
                .httpOnly(httpOnly)
                .secure(secure)
                .sameSite(sameSite)
                .domain(cookieDomain)
                .maxAge(maxAge) // 5일(초 단위)
                .build();
    }

    public void expireCookie(String cookieName, HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from(cookieName, "")
                .path("/")
                .maxAge(0)
                .httpOnly(true)
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }
}
