package com.auth.domain.dto.user;


import java.util.List;

public record CreateUserDto(String document,
                            String password,
                            List<String> roles) {
}
