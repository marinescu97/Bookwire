package com.store.Bookwire.models.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.store.Bookwire.models.entities.User;
import com.store.Bookwire.validators.Unique;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Unique.List({
        @Unique(entity = User.class, fieldName = "username", message = "Username already exists."),
        @Unique(entity = User.class, fieldName = "email", message = "Email already exists.")
})
@Schema(description = "DTO used for updating an authenticated user")
public class UserUpdateDto implements Serializable {
    @JsonIgnore
    private Long id;
    @Schema(example = "johnDoe")
    @Size(max = 20, message = "Username must be maximum 20 characters long.")
    private String username;
    @Schema(example = "johnDoe@gmail.com")
    @Size(max = 50, message = "Email must be maximum 50 characters long.")
    @Email(message = "Invalid email")
    private String email;
    @Schema(example = "password123")
    @Size(max = 100, message = "Password must be maximum 100 characters long.")
    private String password;
}
