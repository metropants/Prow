package me.metropants.prow.controller.controllers;

import lombok.RequiredArgsConstructor;
import me.metropants.prow.entity.entities.User;
import me.metropants.prow.payload.request.UserUpdateRequest;
import me.metropants.prow.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@CrossOrigin("*")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<User> getSelfUser(@AuthenticationPrincipal String username) {
        User user = this.userService.getByUsername(username);
        return ResponseEntity.ok(user);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/{username}")
    public ResponseEntity<User> getUser(@PathVariable String username) {
        User user = this.userService.getByUsername(username);
        return ResponseEntity.ok(user);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = this.userService.getAll();
        return ResponseEntity.ok(users);
    }

    @PatchMapping("/me")
    public ResponseEntity<User> updateSelfUser(@AuthenticationPrincipal String username, @RequestBody UserUpdateRequest request) {
        User updatedUser = this.userService.update(username, request);
        if (updatedUser == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(updatedUser);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PatchMapping("/{username}")
    public ResponseEntity<User> updateUser(@PathVariable String username, @RequestBody UserUpdateRequest request) {
        User updatedUser = this.userService.update(username, request);
        if (updatedUser == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/me")
    public ResponseEntity<?> deleteSelfUser(@AuthenticationPrincipal String username) {
        this.userService.deleteByUsername(username);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{username}")
    public ResponseEntity<?> deleteUserByUsername(@PathVariable String username) {
        this.userService.deleteByUsername(username);
        return ResponseEntity.ok().build();
    }

}
