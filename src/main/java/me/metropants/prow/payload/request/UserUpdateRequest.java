package me.metropants.prow.payload.request;

import me.metropants.prow.entity.entities.User;

import java.util.Set;

public record UserUpdateRequest(String username, String password, Set<User.Role> roles) {}
