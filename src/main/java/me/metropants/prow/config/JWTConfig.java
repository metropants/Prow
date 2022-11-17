package me.metropants.prow.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class JWTConfig {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration:3600}")
    private int expiresIn;

    @Value("${jwt.type:Bearer }")
    private String tokenType;

    @Bean
    public Algorithm getAlgorithm() {
        return Algorithm.HMAC256(this.secret);
    }

    @Bean
    public JWTVerifier getVerifier() {
        return JWT.require(this.getAlgorithm())
                .build();
    }

}
