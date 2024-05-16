package com.auth.infra.security;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

@FunctionalInterface
public interface HttpSecurityConfigure {
    void configure(HttpSecurity httpSecurity) throws Exception;
}