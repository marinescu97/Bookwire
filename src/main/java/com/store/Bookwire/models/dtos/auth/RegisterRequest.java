package com.store.Bookwire.models.dtos.auth;

import com.store.Bookwire.models.entities.User;
import com.store.Bookwire.validators.Unique;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Unique.List({
        @Unique(entity = User.class, fieldName = "username", message = "Username already exists."),
        @Unique(entity = User.class, fieldName = "email", message = "Email already exists.")
})
public class RegisterRequest implements Serializable {
    @NotBlank(message = "Username cannot be blank.")
    @Size(max = 20, message = "Username must be at most 20 characters long.")
    private String username;
    @NotBlank(message = "Email cannot be blank.")
    @Size(max = 50, message = "Email must be at most 50 characters long.")
    @Email(message = "Invalid email")
    private String email;
    @NotBlank(message = "Password cannot be blank.")
    @Size(min = 8, max = 100, message = "Password must be between 8 and 100 characters long.")
    private String password;
}
