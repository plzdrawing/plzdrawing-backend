package org.example.plzdrawing.common.config.web;

import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration c = new CorsConfiguration();
        // ★ Swagger UI / 프런트의 실제 오리진을 명시 (와일드카드 금지: credentials=true와 함께 쓸 수 없음)
        c.setAllowedOrigins(List.of("http://localhost:8080"));
        c.setAllowedMethods(List.of("GET","POST","PUT","DELETE","PATCH","OPTIONS"));
        c.setAllowedHeaders(List.of("*"));
        c.setExposedHeaders(List.of("Set-Cookie","Location"));
        c.setAllowCredentials(true); // 토큰/쿠키 포함 요청이면 true

        UrlBasedCorsConfigurationSource s = new UrlBasedCorsConfigurationSource();
        s.registerCorsConfiguration("/**", c);
        return s;
    }
}
//TODO line13 추후 로드밸런서 ip로 수정..?
