package com.store.Bookwire.models.dtos.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Request DTO for user login")
public class LoginRequest implements Serializable {
    @Schema(description = "Username of the user", example = "johnDoe")
    private String username;
    @Schema(description = "Password of the user", example = "password123")
    private String password;
}