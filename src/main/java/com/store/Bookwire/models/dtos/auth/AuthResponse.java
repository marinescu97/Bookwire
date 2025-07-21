package com.store.Bookwire.models.dtos.auth;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Data transfer object used for receiving a token after login")
public record AuthResponse(String token) {
}