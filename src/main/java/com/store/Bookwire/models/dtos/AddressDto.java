package com.store.Bookwire.models.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO used for adding address to a user profile")
public class AddressDto {
    @Schema(description = "Street name and number", example = "123 Main St")
    @Size(max = 30, message = "Street must be maximum 30 characters long")
    private String street;
    @Schema(description = "Zip code (4 digits)", example = "1234")
    @Pattern(regexp = "\\d{4}", message = "Zip code must be exactly 4 digits")
    private String zipCode;
    @Schema(description = "City name", example = "Springfield")
    @Size(max = 20, message = "City must be maximum 20 characters long")
    private String city;
    @Schema(description = "State or province", example = "Illinois")
    @Size(max = 20, message = "State must be maximum 20 characters long")
    private String state;
}
