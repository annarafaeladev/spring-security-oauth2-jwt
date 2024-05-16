package com.auth.infra.authentication.jwt;

import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth.domain.dto.user.UserDto;
import com.auth.util.ObjectMapperUtil;
import com.auth.domain.dto.auth.LoginResponse;
import com.auth.infra.authentication.userDetails.UserDetailsImpl;
import lombok.AllArgsConstructor;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class JwtTokenService {

    private final JwtDecoder jwtDecoder;
    private final JwtEncoder jwtEncoder;
    private final JwtProperties jwtProperties;
	

    public String generateToken(UserDetailsImpl user) {
        try {

            var scopes = user.getUserEntity().getRoles()
                    .stream()
                    .collect(Collectors.joining(" "));

			UserDto userDto = new UserDto(user.getUserEntity().getId(), user.getUsername(), user.getUserEntity().getRoles());
            var claims = JwtClaimsSet.builder()
                    .issuer(jwtProperties.getIssuer())
                    .subject((String) ObjectMapperUtil.safeWriteValueAsString(userDto))
                    .issuedAt(creationDate())
                    .expiresAt(expirationDate())
                    .claim("scope", scopes)
                    .build();

            return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        } catch (JWTCreationException exception) {
            throw new JWTCreationException("Erro ao gerar token.", exception);
        }
    }
	
	public LoginResponse generateResponseAccessToken(UserDetailsImpl principal) {
		return new LoginResponse(generateToken(principal), Long.parseLong(jwtProperties.getExpires()));
	}

    public boolean validateToken(String token) {
        try {
            boolean verify = false;
            Jwt jwt = jwtDecoder.decode(token);

            if (jwt.getExpiresAt() != null) {
                verify = Instant.now().isBefore(jwt.getExpiresAt());
            }
            return verify;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }


    public String getFromSubjectToken(String token) {
        try {
            Jwt jwt = jwtDecoder.decode(token);

            return jwt.getSubject();
        } catch (OAuth2AuthenticationException e) {
            return null;
        }
    }


    private Instant creationDate() {
        return ZonedDateTime.now(ZoneId.of("America/Recife")).toInstant();
    }

    private Instant expirationDate() {
        return ZonedDateTime.now(ZoneId.of("America/Recife")).plusSeconds(Long.parseLong(jwtProperties.getExpires())).toInstant();
    }

}