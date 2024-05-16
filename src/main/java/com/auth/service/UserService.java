package com.auth.service;

import com.auth.infra.authentication.jwt.JwtTokenService;
import com.auth.domain.dto.user.CreateUserDto;
import com.auth.domain.dto.user.UserDto;
import com.auth.domain.dto.auth.LoginRequest;
import com.auth.domain.dto.auth.LoginResponse;
import com.auth.domain.entity.UserEntity;
import com.auth.infra.authentication.userDetails.UserDetailsImpl;
import com.auth.respository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class UserService {

	private AuthenticationManager authenticationManager;

	private JwtTokenService jwtTokenService;

	private UserRepository userRepository;

	private PasswordEncoder passwordEncoder;

	public LoginResponse authenticateUser(LoginRequest loginUserDto) {
		log.info("authenticateUser() - [INFO] - authenticate user [{}] : ", loginUserDto.document());

		var usernamePassword = new UsernamePasswordAuthenticationToken(loginUserDto.document(), loginUserDto.password());

		var auth = this.authenticationManager.authenticate(usernamePassword);

		var response = jwtTokenService.generateResponseAccessToken((UserDetailsImpl) auth.getPrincipal());
		log.info("authenticateUser() - [INFO] - authenticated user [{}] : ", loginUserDto.document());
		return response;
	}

	public UserDto createUser(CreateUserDto createUserDto) throws BadRequestException {
		if (createUserDto.roles() == null || createUserDto.roles().isEmpty()) {
			log.error("createUser() - [ERROR] - not found role of user [{}] : ", createUserDto);

			throw new BadRequestException("O usuário deve ter pelo menos uma role");
		}

		UserEntity userEntity = buildUser(createUserDto);
		UserEntity saveUser = saveUser(userEntity);

		return buildUserDto(saveUser);
	}
	
	public UserDto getUserDetails(long id) throws Exception {
		Optional<UserEntity> userOpt = userRepository.findById(id);

		if (userOpt.isPresent()) return this.buildUserDto(userOpt.get());

		throw new Exception("user not found");
	}

	private UserDto buildUserDto(UserEntity user) {
		return new UserDto(user.getId(), user.getDocument(), user.getRoles());
	}

	private UserEntity buildUser(CreateUserDto createUserDto) {
		String encryptedPassword = passwordEncoder.encode(createUserDto.password());

		return new UserEntity(createUserDto.document(), encryptedPassword, createUserDto.roles());
	}

	private UserEntity saveUser(UserEntity user) {
		return userRepository.save(user);
	}

}
