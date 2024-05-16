package com.auth.controller;

import com.auth.service.UserService;
import com.auth.domain.dto.user.CreateUserDto;
import com.auth.domain.dto.auth.LoginRequest;
import com.auth.domain.dto.auth.LoginResponse;
import com.auth.domain.dto.user.UserDto;
import lombok.AllArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@AllArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        LoginResponse loginResponse = userService.authenticateUser(request);
        return ResponseEntity.ok(loginResponse);
    }


    @PostMapping("/")
    public ResponseEntity<UserDto> create(@RequestBody CreateUserDto createUserDto) throws BadRequestException {
		UserDto user = userService.createUser(createUserDto);
        return ResponseEntity.ok(user);
    }

	@GetMapping("/{id}")
	public ResponseEntity<UserDto> userDetails(@PathVariable long id) throws Exception {
		UserDto user = userService.getUserDetails(id);
		return ResponseEntity.ok(user);
	}

    @GetMapping("/permissao-admin")
    @PreAuthorize("hasAuthority('SCOPE_LEADER') or hasAuthority('SCOPE_CONSULTANT')") // EXAMPLE ENDPOINT WITH AUTHORITIES
    public ResponseEntity<String> teste() {
        return ResponseEntity.ok("Usuario tem permissao admin");
    }

}


