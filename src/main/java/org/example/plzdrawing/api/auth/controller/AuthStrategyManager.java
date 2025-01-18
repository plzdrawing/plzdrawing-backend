package org.example.plzdrawing.api.auth.controller;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.example.plzdrawing.api.auth.service.strategy.AuthService;
import org.example.plzdrawing.domain.member.Provider;
import org.springframework.stereotype.Component;

@Component
public class AuthStrategyManager {

    private final Map<Provider, AuthService> strategyMap;

    public AuthStrategyManager(List<AuthService> strategies) {
        strategyMap = strategies.stream()
                .collect(Collectors.toMap(AuthService::getProviderName, Function.identity()));
    }

    public AuthService getAuthService(Provider provider) {
        return strategyMap.get(provider);
    }
}
