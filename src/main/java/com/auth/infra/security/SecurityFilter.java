package com.auth.infra.security;

import com.auth.infra.authentication.userDetails.UserDetailsImpl;
import com.auth.infra.authentication.userDetails.UserDetailsServiceImpl;
import com.auth.infra.authentication.jwt.JwtTokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@AllArgsConstructor
@Component
public class SecurityFilter extends OncePerRequestFilter {

    JwtTokenService tokenService;

    UserDetailsServiceImpl customUserDetailsService;

    public static final String[] ENDPOINTS_WITH_AUTHENTICATION_NOT_REQUIRED = {
            "/users/login", 
            "/users"
    };

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		var token = this.recoverToken(request);

        if (token == null) {
            filterChain.doFilter(request, response);
            return;
        }

        if (!tokenService.validateToken(token)) {
            throw new RuntimeException("token expirado.");
        }
        var document = tokenService.getFromSubjectToken(token);
		UserDetailsImpl userDetails = customUserDetailsService.loadUserByUsername(document);

        var authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }


    private String recoverToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null) {
            return authorizationHeader.replace("Bearer ", "");
        }
        return null;
    }

    private boolean checkIfEndpointIsPublic(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        return Arrays.asList(ENDPOINTS_WITH_AUTHENTICATION_NOT_REQUIRED).contains(requestURI);
    }
}
