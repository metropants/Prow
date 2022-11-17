package me.metropants.prow.service;

import com.auth0.jwt.interfaces.DecodedJWT;
import me.metropants.prow.payload.request.JWTRefreshRequest;
import me.metropants.prow.payload.response.JWTResponse;
import org.jetbrains.annotations.NotNull;

public interface JWTService {

    enum TokenType {

        ACCESS, REFRESH

    }

    /**
     * @param username the username of the user.
     * @return a new {@link JWTResponse}.
     */
    JWTResponse generateTokens(@NotNull String username);

    /**
     * @param request the {@link JWTRefreshRequest} to validate.
     * @return a new {@link JWTResponse}.
     */
    JWTResponse refreshTokens(@NotNull JWTRefreshRequest request);

    /**
     * @param token the token to get the type for.
     * @return the {@link TokenType} of the token.
     */
    TokenType getTokenType(@NotNull String token);

    /**
     * @param token the token to decode.
     * @return A {@link DecodedJWT} object from the provided token.
     */
    DecodedJWT decodeToken(@NotNull String token);

}
