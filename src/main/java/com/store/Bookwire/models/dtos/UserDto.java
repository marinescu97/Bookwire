package com.store.Bookwire.models.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Schema(description = "DTO used for displaying user data")
public class UserDto extends UserUpdateDto {
    private AddressDto address;
}
