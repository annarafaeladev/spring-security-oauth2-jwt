package com.auth.infra.security;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.auth.infra.authentication.userDetails.UserDetailsServiceImpl;
import com.auth.infra.authentication.jwt.JwtProperties;
import com.auth.infra.authentication.jwt.JwtTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@EnableConfigurationProperties(JwtProperties.class)
public class SecurityConfig {


	@Autowired
	UserDetailsServiceImpl customUserDetailsService;

	@Autowired
	JwtProperties jwtProperties;

	@Bean
	public HttpSecurityConfigure httpSecurityConfigurer() {
		return httpSecurity -> httpSecurity.authorizeHttpRequests(authorize -> authorize
				.requestMatchers(HttpMethod.POST, "/users/").permitAll()
				.requestMatchers(HttpMethod.POST, "/users/login").permitAll()
				.anyRequest().authenticated())
			.csrf(AbstractHttpConfigurer::disable)
			.oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.addFilterBefore(securityFilter(), UsernamePasswordAuthenticationFilter.class);

	}	

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public JwtDecoder jwtDecoder() {
		return NimbusJwtDecoder.withPublicKey(jwtProperties.getPublicKey()).build();
	}

	@Bean
	public JwtEncoder jwtEncoder() {
		JWK jwk = new RSAKey.Builder(jwtProperties.getPublicKey()).privateKey(jwtProperties.getPrivateKey()).build();
		var jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
		return new NimbusJwtEncoder(jwks);
	}

	@Bean
	JwtTokenService jwtTokenService(JwtEncoder jwtEncoder, JwtDecoder jwtDecoder) {
		return new JwtTokenService(jwtDecoder, jwtEncoder, jwtProperties);
	}

	@Bean
	public SecurityFilter securityFilter() {
		return new SecurityFilter(jwtTokenService(jwtEncoder(), jwtDecoder()), customUserDetailsService);
	}

}


