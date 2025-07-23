package com.store.Bookwire.models.dtos.auth;

import com.store.Bookwire.models.entities.User;
import com.store.Bookwire.validators.Unique;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Schema(description = "Request DTO used for user login")
@Unique.List({
        @Unique(entity = User.class, fieldName = "username", message = "Username already exists.")
})
public class LoginRequest implements Serializable {
    @Schema(description = "Username of the user", example = "johnDoe")
    @NotBlank(message = "Username cannot be blank.")
    @Size(max = 20, message = "Username must be at most 20 characters long.")
    private String username;

    @Schema(description = "Password of the user", example = "password123")
    @NotBlank(message = "Password cannot be blank.")
    @Size(min = 8, max = 100, message = "Password must be between 8 and 100 characters long.")
    private String password;
}