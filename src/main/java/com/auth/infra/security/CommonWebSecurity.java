package com.auth.infra.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;

@Configuration
public class CommonWebSecurity {

    private static final String[] ALLOWED_REQUEST_MATCHERS = {"/actuator/**"};

    @Bean
    public com.auth.infra.security.HttpSecurityConfigure commonHttpSecurityConfigurer() {
        return httpSecurity -> httpSecurity.authorizeHttpRequests(
                authorize -> authorize.requestMatchers(ALLOWED_REQUEST_MATCHERS).permitAll());
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity httpSecurity, List<com.auth.infra.security.HttpSecurityConfigure> httpSecurityConfigures) throws Exception {
        for (var configure : httpSecurityConfigures) {
            configure.configure(httpSecurity);
        }
        return httpSecurity.build();
    }
}