package me.metropants.prow.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.metropants.prow.config.JWTConfig;
import me.metropants.prow.entity.entities.User;
import me.metropants.prow.payload.request.JWTRefreshRequest;
import me.metropants.prow.payload.response.JWTResponse;
import me.metropants.prow.service.JWTService;
import me.metropants.prow.service.UserService;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class JWTServiceImpl implements JWTService {

    private final JWTConfig config;
    private final UserService userService;

    private DecodedJWT decode(@NotNull String token) {
        JWTVerifier verifier = this.config.getVerifier();
        return verifier.verify(token);
    }

    /**
     * @param token the token to validate.
     * @return true if the token is not expired, false otherwise.
     */
    private boolean isTokenExpired(@NotNull String token) {
        DecodedJWT decoded = this.decode(token);
        if (decoded.getExpiresAt() == null) {
            return false;
        }

        return decoded.getExpiresAt().before(new Date());
    }

    @Override
    public JWTResponse generateTokens(@NotNull String username) {
        User user = this.userService.getByUsername(username);

        final Algorithm algorithm = this.config.getAlgorithm();

        final String accessToken = JWT.create()
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + this.config.getExpiresIn() * 1000L))
                .withSubject(user.getUsername())
                .withClaim("type", "access")
                .withClaim("roles", user.getRoles().stream().map(Enum::name).toList())
                .sign(algorithm);

        final String refreshToken = JWT.create()
                .withIssuedAt(new Date())
                .withSubject(user.getUsername())
                .withClaim("type", "refresh")
                .sign(algorithm);

        return JWTResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType(StringUtils.trimWhitespace(this.config.getTokenType()))
                .expiresIn(this.config.getExpiresIn())
                .build();
    }

    @Override
    public JWTResponse refreshTokens(@NotNull JWTRefreshRequest request) {
        final String refreshToken = request.refreshToken();
        if (this.isTokenExpired(refreshToken)) {
            throw new IllegalArgumentException("Refresh token is expired.");
        }

        DecodedJWT decoded = this.decode(refreshToken);
        if (decoded.getClaim("type") == null) {
            throw new IllegalArgumentException("Provided refresh token is invalid.");
        }

        TokenType type = this.getTokenType(refreshToken);
        if (type != TokenType.REFRESH) {
            throw new IllegalArgumentException("Provided refresh token is invalid.");
        }

        if (decoded.getSubject() == null) {
            throw new IllegalArgumentException("Provided refresh token is invalid.");
        }

        String username = decoded.getSubject();
        log.info("Refreshing tokens for user: {}.", username);
        return this.generateTokens(username);
    }

    @Override
    public TokenType getTokenType(@NotNull String token) {
        DecodedJWT decoded = this.decode(token);
        String type = decoded.getClaim("type").asString();

        return TokenType.valueOf(type.toUpperCase());
    }

    @Override
    public DecodedJWT decodeToken(@NotNull String token) {
        return this.decode(token);
    }


}
