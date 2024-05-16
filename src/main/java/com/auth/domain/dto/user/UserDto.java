package com.auth.domain.dto.user;

import java.util.List;

public record UserDto(long id, String document, List<String> roles) {
}
