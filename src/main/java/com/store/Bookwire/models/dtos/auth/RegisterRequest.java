package com.store.Bookwire.models.dtos.auth;

import com.store.Bookwire.models.entities.User;
import com.store.Bookwire.validators.Unique;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Unique.List({
        @Unique(entity = User.class, fieldName = "email", message = "Email already exists.")
})
@Schema(description = "Request DTO used for user registration")
public class RegisterRequest extends LoginRequest {
    @NotBlank(message = "Email cannot be blank.")
    @Size(max = 50, message = "Email must be at most 50 characters long.")
    @Email(message = "Invalid email")
    @Schema(description = "Email of the user", example = "johnDoe@gmail.com")
    private String email;
}
