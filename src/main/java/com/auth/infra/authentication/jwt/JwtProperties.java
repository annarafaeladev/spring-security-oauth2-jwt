package com.auth.infra.authentication.jwt;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;


@Configuration
@ConfigurationProperties(prefix = "jwt")
@Data
public class  JwtProperties{
	RSAPublicKey publicKey;
	RSAPrivateKey privateKey;
	String issuer;
	String expires;	
}
