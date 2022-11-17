package me.metropants.prow.payload.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record JWTRefreshRequest(@JsonProperty("refresh_token") String refreshToken) {}
