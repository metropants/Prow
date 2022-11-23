package me.metropants.prow.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.metropants.prow.entity.entities.User;
import me.metropants.prow.payload.request.RegisterRequest;
import me.metropants.prow.payload.request.UserUpdateRequest;
import me.metropants.prow.repository.UserRepository;
import me.metropants.prow.service.UserService;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final String USERNAME_REGEX = "[a-zA-Z0-9]{3,32}$";

    private final UserRepository repository;
    private final PasswordEncoder encoder;

    private void checkUsername(@NotNull String username) {
        if (!username.matches(USERNAME_REGEX)) {
            throw new IllegalArgumentException("Username must be between 3 and 32 characters and only contain letters and numbers.");
        }
    }

    @Override
    public User save(@NotNull RegisterRequest request) {
        String username = request.username();
        if (this.repository.existsByUsername(username)) {
            throw new IllegalArgumentException("Username already exists.");
        }

        checkUsername(username);

        User user = MAPPER.map(request);
        if (user == null) {
            log.error("Failed to map request to user. \nRequest: {}", request);
            return null;
        }

        log.info("Saving user: {}", user);
        user.setPassword(this.encoder.encode(user.getPassword()));
        return this.repository.save(user);
    }

    @Override
    public User update(@NotNull String username, @NotNull UserUpdateRequest request) {
        return this.repository.findUserByUsername(username)
                .map(user -> {
                    if (!user.getRoles().contains(User.Role.ADMIN) && request.roles() != null) {
                        throw new IllegalArgumentException("You do not have permission to update roles.");
                    }

                    User updated = MAPPER.map(request);
                    if (updated == null) {
                        log.error("Failed to map request to user. \nRequest: {}", request);
                        return null;
                    }

                    updated.setId(user.getId());
                    if (updated.getUsername() == null) {
                        updated.setUsername(user.getUsername());
                    } else {
                        checkUsername(updated.getUsername());
                    }

                    if (request.password() == null) {
                        updated.setPassword(user.getPassword());
                    } else {
                        updated.setPassword(this.encoder.encode(request.password()));
                    }

                    if (updated.getRoles() == null) {
                        updated.setRoles(user.getRoles());
                    }
                    return this.repository.save(updated);
                })
                .orElseThrow(() -> new UsernameNotFoundException("User with username " + username + " not found."));
    }

    @Override
    public void deleteByUsername(@NotNull String username) {
        if (!this.repository.existsByUsername(username)) {
            throw new UsernameNotFoundException("User with username " + username + " does not exist.");
        }

        this.repository.deleteByUsername(username);
    }

    @Override
    public boolean existsByUsername(@NotNull String username) {
        return this.repository.existsByUsername(username);
    }

    @Override
    public User getByUsername(@NotNull String username) {
        return this.repository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with username " + username + " does not exist."));
    }

    @Override
    public List<User> getAll() {
        return this.repository.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.repository.findUserByUsername(username)
                .map(user -> org.springframework.security.core.userdetails.User
                        .withUsername(user.getUsername())
                        .password(user.getPassword())
                        .roles(user.getRoles().stream().map(Enum::name).toArray(String[]::new))
                        .build())
                .orElseThrow(() -> new UsernameNotFoundException("User with username " + username + " not found."));
    }

}
