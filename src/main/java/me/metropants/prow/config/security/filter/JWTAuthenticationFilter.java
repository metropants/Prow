package me.metropants.prow.config.security.filter;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.metropants.prow.config.JWTConfig;
import me.metropants.prow.service.JWTService;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    private final JWTConfig config;
    private final JWTService jwtService;

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain chain) throws ServletException, IOException {
        if (!checkHeader(request)) {
            chain.doFilter(request, response);
            return;
        }

        final String accessToken = this.getToken(request);
        try {
            JWTService.TokenType type = this.jwtService.getTokenType(accessToken);
            if (!type.equals(JWTService.TokenType.ACCESS)) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                return;
            }

            DecodedJWT decoded = this.jwtService.decodeToken(accessToken);
            String username = decoded.getSubject();
            List<SimpleGrantedAuthority> authorities = Arrays.stream(decoded.getClaim("roles").asArray(String.class))
                    .map(SimpleGrantedAuthority::new)
                    .toList();

            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(token);
        } catch (JWTVerificationException e) {
            SecurityContextHolder.clearContext();
        }
        chain.doFilter(request, response);
    }

    private boolean checkHeader(@NotNull HttpServletRequest request) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        String tokenType = this.config.getTokenType();
        return header != null && header.startsWith(tokenType);
    }

    private @NotNull String getToken(@NotNull HttpServletRequest request) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header == null) {
            throw new IllegalArgumentException("No Authorization header found.");
        }

        return header.substring(this.config.getTokenType().length());
    }

}
