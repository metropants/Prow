package me.metropants.prow.controller.controllers;

import lombok.RequiredArgsConstructor;
import me.metropants.prow.entity.entities.User;
import me.metropants.prow.payload.request.JWTRefreshRequest;
import me.metropants.prow.payload.request.LoginRequest;
import me.metropants.prow.payload.request.RegisterRequest;
import me.metropants.prow.payload.response.JWTResponse;
import me.metropants.prow.service.JWTService;
import me.metropants.prow.service.UserService;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<JWTResponse> login(@RequestBody @NotNull LoginRequest request) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(request.username(), request.password());
        Authentication authentication = this.authenticationManager.authenticate(token);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        JWTResponse response = this.jwtService.generateTokens(authentication.getName());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody @NotNull RegisterRequest request) {
        User user = this.userService.save(request);
        if (user == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(user);
    }

    @PostMapping("/refresh")
    public ResponseEntity<JWTResponse> refresh(@RequestBody @NotNull JWTRefreshRequest request) {
        JWTResponse response = this.jwtService.refreshTokens(request);
        return ResponseEntity.ok(response);
    }

}
