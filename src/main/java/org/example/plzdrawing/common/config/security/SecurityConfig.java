package org.example.plzdrawing.common.config.security;

import org.example.plzdrawing.common.config.web.WebConfig;
import org.example.plzdrawing.common.oauth.CustomOAuth2UserService;
import org.example.plzdrawing.common.oauth.handler.CustomSuccessHandler;
import org.example.plzdrawing.util.jwt.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private static final String[] AUTH_WHITELIST = {
            "/",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/api/auth/email/v1/**"
    };

    private static final String[] AUTH_TEMP = {
        "/api/auth/v1/signup"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, WebConfig webConfig,
            CustomOAuth2UserService customOAuth2UserService, JwtAuthenticationFilter jwtAuthenticationFilter,
            CustomSuccessHandler customSuccessHandler) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .oauth2Login(httpSecurityOAuth2LoginConfigurer -> {
                    httpSecurityOAuth2LoginConfigurer.userInfoEndpoint(
                            userInfoEndpointConfig -> {
                                userInfoEndpointConfig.userService(customOAuth2UserService);
                            }
                    ).successHandler(
                            customSuccessHandler
                    );
                })
                .authorizeHttpRequests((authorizeRequests) ->
                        authorizeRequests.requestMatchers(AUTH_WHITELIST).permitAll()
                                .requestMatchers(AUTH_TEMP).hasRole("TEMP")
                                .anyRequest().hasRole("MEMBER"))
                .addFilterBefore(jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
