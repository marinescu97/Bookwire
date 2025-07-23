package com.store.Bookwire.models.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.store.Bookwire.models.dtos.auth.RegisterRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Schema(description = "DTO used for updating an authenticated user")
public class UserUpdateDto extends RegisterRequest {
    @JsonIgnore
    @Schema(description = "Id of the user", hidden = true)
    private Long id;
}
