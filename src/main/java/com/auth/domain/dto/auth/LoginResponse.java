package com.auth.domain.dto.auth;

public record LoginResponse(String accessToken, long expiresIn) {
}
